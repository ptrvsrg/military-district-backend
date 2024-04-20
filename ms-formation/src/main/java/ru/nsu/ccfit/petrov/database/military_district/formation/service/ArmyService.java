package ru.nsu.ccfit.petrov.database.military_district.formation.service;

import static ru.nsu.ccfit.petrov.database.military_district.formation.util.SpecPageSortUtils.generateArmySpec;
import static ru.nsu.ccfit.petrov.database.military_district.formation.util.SpecPageSortUtils.generatePageable;
import static ru.nsu.ccfit.petrov.database.military_district.formation.util.SpecPageSortUtils.generateSort;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.ArmyFilter;
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.ArmyInput;
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.Pagination;
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.Sorting;
import ru.nsu.ccfit.petrov.database.military_district.formation.exception.ArmyAlreadyExistsException;
import ru.nsu.ccfit.petrov.database.military_district.formation.exception.ArmyNotFoundException;
import ru.nsu.ccfit.petrov.database.military_district.formation.mapper.ArmyMapper;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.entity.Army;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.repository.ArmyRepository;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.repository.BrigadeRepository;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.repository.CorpsRepository;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.repository.DivisionRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ArmyService implements GraphQLService {

  private static final List<String> availableSortFields = List.of("commander.mbn", "name");

  private final ArmyRepository armyRepository;
  private final BrigadeRepository brigadeRepository;
  private final CorpsRepository corpsRepository;
  private final DivisionRepository divisionRepository;
  private final ArmyMapper armyMapper;

  public List<Army> getAll(ArmyFilter filter, Pagination pagination, List<Sorting> sorts) {
    log.info("Get all armies: filter={}, pagination={}, sorts={}", filter, pagination, sorts);
    var sort = generateSort(sorts, availableSortFields);
    var pageable = generatePageable(pagination, sort);
    var spec = generateArmySpec(filter);
    return armyRepository.findAll(spec, pageable, sort);
  }

  public long getAllCount(ArmyFilter filter) {
    log.info("Get all armies count: filter={}", filter);
    var spec = generateArmySpec(filter);
    return armyRepository.count(spec);
  }

  public Army getByName(@NonNull String name) {
    log.info("Get army by name: name={}", name);
    return armyRepository.findByName(name).orElse(null);
  }

  @Transactional
  public Army create(@Valid @NonNull ArmyInput armyInput) {
    log.info("Create army: input={}", armyInput);
    if (armyRepository.existsByName(armyInput.getName())) {
      throw new ArmyAlreadyExistsException();
    }

    var army = armyMapper.toEntity(armyInput);
    army.setBrigades(brigadeRepository.findByNameIn(armyInput.getBrigades()));
    army.setCorps(corpsRepository.findByNameIn(armyInput.getCorps()));
    army.setDivisions(divisionRepository.findByNameIn(armyInput.getDivisions()));

    return armyRepository.save(army);
  }

  @Transactional
  public Army update(@NonNull String name, @Valid @NonNull ArmyInput armyInput) {
    log.info("Update army: name={}, input={}", name, armyInput);
    var army = armyRepository.findByName(name).orElseThrow(ArmyNotFoundException::new);
    if (!name.equals(armyInput.getName()) && armyRepository.existsByName(armyInput.getName())) {
      throw new ArmyAlreadyExistsException();
    }

    armyMapper.partialUpdate(armyInput, army);
    army.setBrigades(brigadeRepository.findByNameIn(armyInput.getBrigades()));
    army.setCorps(corpsRepository.findByNameIn(armyInput.getCorps()));
    army.setDivisions(divisionRepository.findByNameIn(armyInput.getDivisions()));

    return armyRepository.save(army);
  }

  @Transactional
  public long delete(@NonNull String name) {
    log.info("Delete army: name={}", name);
    return armyRepository.deleteByName(name);
  }

  @Override
  public Object resolveReference(@NonNull Map<String, Object> reference) {
    log.info("Resolve reference: reference={}", reference);
    if (reference.get("name") instanceof String name) {
      return getByName(name);
    }
    return null;
  }
}
