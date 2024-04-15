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
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.DivisionDto;
import ru.nsu.ccfit.petrov.database.military_district.formation.exception.DivisionAlreadyExistsException;
import ru.nsu.ccfit.petrov.database.military_district.formation.exception.DivisionNotFoundException;
import ru.nsu.ccfit.petrov.database.military_district.formation.mapper.DivisionMapper;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.entity.Division;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.repository.ArmyRepository;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.repository.DivisionRepository;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.repository.UnitRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DivisionService implements GraphQLService {

  private static final List<String> availableSortFields = List.of("commander.mbn", "name");

  private final DivisionRepository divisionRepository;
  private final ArmyRepository armyRepository;
  private final UnitRepository unitRepository;
  private final DivisionMapper divisionMapper;

  public List<Division> getAll(
      String name,
      String commander,
      Integer page,
      Integer pageSize,
      String sortField,
      Boolean sortAsc) {
    var sort = generateSort(sortField, sortAsc, availableSortFields);
    var pageable = generatePageable(page, pageSize, sort);
    var spec = generateSpecification(name, commander);
    return divisionRepository.findAll(spec, pageable, sort);
  }

  public long getAllCount(String name, String commander) {
    var spec = generateSpecification(name, commander);
    if (spec == null) {
      return divisionRepository.count();
    }
    return divisionRepository.count(spec);
  }

  public Division getByName(@NonNull String name) {
    return divisionRepository.findByName(name).orElse(null);
  }

  @Transactional
  public Division create(@Valid @NonNull DivisionDto divisionDto) {
    if (divisionRepository.existsByName(divisionDto.getName())) {
      throw new DivisionAlreadyExistsException();
    }

    var division = divisionMapper.toEntity(divisionDto);
    division.setArmies(armyRepository.findByNameIn(divisionDto.getArmies()));
    division.setUnits(unitRepository.findByNameIn(divisionDto.getUnits()));

    return divisionRepository.save(division);
  }

  @Transactional
  public Division update(@NonNull String name, @Valid @NonNull DivisionDto divisionDto) {
    var division = divisionRepository.findByName(name).orElseThrow(DivisionNotFoundException::new);
    if (!name.equals(divisionDto.getName())
        && divisionRepository.existsByName(divisionDto.getName())) {
      throw new DivisionAlreadyExistsException();
    }

    divisionMapper.partialUpdate(divisionDto, division);
    division.setArmies(armyRepository.findByNameIn(divisionDto.getArmies()));
    division.setUnits(unitRepository.findByNameIn(divisionDto.getUnits()));

    return divisionRepository.save(division);
  }

  @Transactional
  public long delete(@NonNull String name) {
    return divisionRepository.deleteByName(name);
  }

  private Specification<Division> generateSpecification(String name, String commander) {
    Specification<Division> spec = null;
    if (Objects.nonNull(name)) {
      spec = (root, query, builder) -> builder.like(root.get("name"), "%" + name + "%");
    }
    if (Objects.nonNull(commander)) {
      Specification<Division> newSpec =
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
