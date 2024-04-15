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
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.BrigadeDto;
import ru.nsu.ccfit.petrov.database.military_district.formation.exception.BrigadeAlreadyExistsException;
import ru.nsu.ccfit.petrov.database.military_district.formation.exception.BrigadeNotFoundException;
import ru.nsu.ccfit.petrov.database.military_district.formation.mapper.BrigadeMapper;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.entity.Brigade;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.repository.ArmyRepository;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.repository.BrigadeRepository;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.repository.UnitRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BrigadeService implements GraphQLService {

  private static final List<String> availableSortFields = List.of("commander.mbn", "name");

  private final BrigadeRepository brigadeRepository;
  private final ArmyRepository armyRepository;
  private final UnitRepository unitRepository;
  private final BrigadeMapper brigadeMapper;

  public List<Brigade> getAll(
      String name,
      String commander,
      Integer page,
      Integer pageSize,
      String sortField,
      Boolean sortAsc) {
    var sort = generateSort(sortField, sortAsc, availableSortFields);
    var pageable = generatePageable(page, pageSize, sort);
    var spec = generateSpecification(name, commander);
    return brigadeRepository.findAll(spec, pageable, sort);
  }

  public long getAllCount(String name, String commander) {
    var spec = generateSpecification(name, commander);
    if (spec == null) {
      return brigadeRepository.count();
    }
    return brigadeRepository.count(spec);
  }

  public Brigade getByName(@NonNull String name) {
    return brigadeRepository.findByName(name).orElse(null);
  }

  @Transactional
  public Brigade create(@Valid @NonNull BrigadeDto brigadeDto) {
    if (brigadeRepository.existsByName(brigadeDto.getName())) {
      throw new BrigadeAlreadyExistsException();
    }

    var brigade = brigadeMapper.toEntity(brigadeDto);
    brigade.setArmies(armyRepository.findByNameIn(brigadeDto.getArmies()));
    brigade.setUnits(unitRepository.findByNameIn(brigadeDto.getUnits()));

    return brigadeRepository.save(brigade);
  }

  @Transactional
  public Brigade update(@NonNull String name, @Valid @NonNull BrigadeDto brigadeDto) {
    var brigade = brigadeRepository.findByName(name).orElseThrow(BrigadeNotFoundException::new);
    if (!name.equals(brigadeDto.getName())
        && brigadeRepository.existsByName(brigadeDto.getName())) {
      throw new BrigadeAlreadyExistsException();
    }

    brigadeMapper.partialUpdate(brigadeDto, brigade);
    brigade.setArmies(armyRepository.findByNameIn(brigadeDto.getArmies()));
    brigade.setUnits(unitRepository.findByNameIn(brigadeDto.getUnits()));

    return brigadeRepository.save(brigade);
  }

  @Transactional
  public long delete(@NonNull String name) {
    return brigadeRepository.deleteByName(name);
  }

  private Specification<Brigade> generateSpecification(String name, String commander) {
    Specification<Brigade> spec = null;
    if (Objects.nonNull(name)) {
      spec = (root, query, builder) -> builder.like(root.get("name"), "%" + name + "%");
    }
    if (Objects.nonNull(commander)) {
      Specification<Brigade> newSpec =
          (root, query, builder) -> builder.like(root.get("commander.mbn"), "%" + commander + "%");
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
