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
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.CorpsDto;
import ru.nsu.ccfit.petrov.database.military_district.formation.exception.CorpsAlreadyExistsException;
import ru.nsu.ccfit.petrov.database.military_district.formation.exception.CorpsNotFoundException;
import ru.nsu.ccfit.petrov.database.military_district.formation.mapper.CorpsMapper;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.entity.Corps;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.repository.ArmyRepository;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.repository.CorpsRepository;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.repository.UnitRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CorpsService implements GraphQLService {

  private static final List<String> availableSortFields = List.of("commander.mbn", "name");

  private final CorpsRepository corpsRepository;
  private final ArmyRepository armyRepository;
  private final UnitRepository unitRepository;
  private final CorpsMapper corpsMapper;

  public List<Corps> getAll(
      String name,
      String commander,
      Integer page,
      Integer pageSize,
      String sortField,
      Boolean sortAsc) {
    var sort = generateSort(sortField, sortAsc, availableSortFields);
    var pageable = generatePageable(page, pageSize, sort);
    var spec = generateSpecification(name, commander);
    return corpsRepository.findAll(spec, pageable, sort);
  }

  public long getAllCount(String name, String commander) {
    var spec = generateSpecification(name, commander);
    if (spec == null) {
      return corpsRepository.count();
    }
    return corpsRepository.count(spec);
  }

  public Corps getByName(@NonNull String name) {
    return corpsRepository.findByName(name).orElse(null);
  }

  @Transactional
  public Corps create(@Valid @NonNull CorpsDto corpsDto) {
    if (corpsRepository.existsByName(corpsDto.getName())) {
      throw new CorpsAlreadyExistsException();
    }

    var corps = corpsMapper.toEntity(corpsDto);
    corps.setArmies(armyRepository.findByNameIn(corpsDto.getArmies()));
    corps.setUnits(unitRepository.findByNameIn(corpsDto.getUnits()));

    return corpsRepository.save(corps);
  }

  @Transactional
  public Corps update(@NonNull String name, @Valid @NonNull CorpsDto corpsDto) {
    var corps = corpsRepository.findByName(name).orElseThrow(CorpsNotFoundException::new);
    if (!name.equals(corpsDto.getName()) && corpsRepository.existsByName(corpsDto.getName())) {
      throw new CorpsAlreadyExistsException();
    }

    corpsMapper.partialUpdate(corpsDto, corps);
    corps.setArmies(armyRepository.findByNameIn(corpsDto.getArmies()));
    corps.setUnits(unitRepository.findByNameIn(corpsDto.getUnits()));

    return corpsRepository.save(corps);
  }

  @Transactional
  public long delete(@NonNull String name) {
    return corpsRepository.deleteByName(name);
  }

  private Specification<Corps> generateSpecification(String name, String commander) {
    Specification<Corps> spec = null;
    if (Objects.nonNull(name)) {
      spec = (root, query, builder) -> builder.like(root.get("name"), "%" + name + "%");
    }
    if (Objects.nonNull(commander)) {
      Specification<Corps> newSpec =
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
