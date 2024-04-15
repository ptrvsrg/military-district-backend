package ru.nsu.ccfit.petrov.database.military_district.formation.service;

import static ru.nsu.ccfit.petrov.database.military_district.formation.util.SpecPageSortUtils.generatePageable;
import static ru.nsu.ccfit.petrov.database.military_district.formation.util.SpecPageSortUtils.generateSort;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.SquadDto;
import ru.nsu.ccfit.petrov.database.military_district.formation.exception.PlatoonNotFoundException;
import ru.nsu.ccfit.petrov.database.military_district.formation.exception.SquadAlreadyExistsException;
import ru.nsu.ccfit.petrov.database.military_district.formation.exception.SquadNotFoundException;
import ru.nsu.ccfit.petrov.database.military_district.formation.mapper.SquadMapper;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.entity.Squad;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.repository.PlatoonRepository;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.repository.SquadRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SquadService implements GraphQLService {

  private static final List<String> availableSortFields =
      List.of("commander.mbn", "name", "platoon.name");

  private final SquadRepository squadRepository;
  private final PlatoonRepository platoonRepository;
  private final SquadMapper squadMapper;

  public List<Squad> getAll(
      String name,
      String commander,
      String platoon,
      Integer page,
      Integer pageSize,
      String sortField,
      Boolean sortAsc) {
    var sort = generateSort(sortField, sortAsc, availableSortFields);
    var pageable = generatePageable(page, pageSize, sort);
    var spec = generateSpecification(name, commander, platoon);
    return squadRepository.findAll(spec, pageable, sort);
  }

  public long getAllCount(String name, String commander, String platoon) {
    var spec = generateSpecification(name, commander, platoon);
    if (spec == null) {
      return squadRepository.count();
    }
    return squadRepository.count(spec);
  }

  public Squad getByName(@NonNull String name) {
    return squadRepository.findByName(name).orElse(null);
  }

  @Transactional
  public Squad create(@Valid @NonNull SquadDto squadDto) {
    if (squadRepository.existsByName(squadDto.getName())) {
      throw new SquadAlreadyExistsException();
    }

    var squad = squadMapper.toEntity(squadDto);
    squad.setPlatoon(
        platoonRepository
            .findByName(squadDto.getPlatoon())
            .orElseThrow(PlatoonNotFoundException::new));

    return squadRepository.save(squad);
  }

  @Transactional
  public Squad update(@NonNull String name, @Valid @NonNull SquadDto squadDto) {
    var squad = squadRepository.findByName(name).orElseThrow(SquadNotFoundException::new);
    if (!name.equals(squadDto.getName()) && squadRepository.existsByName(squadDto.getName())) {
      throw new SquadAlreadyExistsException();
    }

    squadMapper.partialUpdate(squadDto, squad);
    squad.setPlatoon(
        platoonRepository
            .findByName(squadDto.getPlatoon())
            .orElseThrow(PlatoonNotFoundException::new));

    return squadRepository.save(squad);
  }

  @Transactional
  public long delete(@NonNull String name) {
    return squadRepository.deleteByName(name);
  }

  private Specification<Squad> generateSpecification(
      String name, String commander, String platoon) {
    Specification<Squad> spec = null;
    if (Objects.nonNull(name)) {
      spec = (root, query, builder) -> builder.like(root.get("name"), "%" + name + "%");
    }
    if (Objects.nonNull(commander)) {
      Specification<Squad> newSpec =
          (root, query, builder) -> builder.like(root.get("commander.mbn"), "%" + commander + "%");
      spec = Objects.isNull(spec) ? newSpec : spec.and(newSpec);
    }
    if (Objects.nonNull(platoon)) {
      Specification<Squad> newSpec =
          (root, query, builder) -> builder.like(root.get("platoon.name"), "%" + platoon + "%");
      spec = Objects.isNull(spec) ? newSpec : spec.and(newSpec);
    }
    return spec;
  }

  @Override
  public Object resolveReference(@NonNull Map<String, Object> reference) {
    if (reference.get("name") instanceof String name) {
      return getByName(name);
    }
    return null;
  }
}
