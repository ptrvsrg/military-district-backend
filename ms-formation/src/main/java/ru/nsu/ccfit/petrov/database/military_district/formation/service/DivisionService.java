package ru.nsu.ccfit.petrov.database.military_district.formation.service;

import static ru.nsu.ccfit.petrov.database.military_district.formation.util.SpecPageSortUtils.generateDivisionSpec;
import static ru.nsu.ccfit.petrov.database.military_district.formation.util.SpecPageSortUtils.generatePageable;
import static ru.nsu.ccfit.petrov.database.military_district.formation.util.SpecPageSortUtils.generateSort;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.DivisionFilter;
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.DivisionInput;
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.Pagination;
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.Sorting;
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
@Slf4j
public class DivisionService implements GraphQLService {

  private static final List<String> availableSortFields = List.of("commander.mbn", "name");

  private final DivisionRepository divisionRepository;
  private final ArmyRepository armyRepository;
  private final UnitRepository unitRepository;
  private final DivisionMapper divisionMapper;

  @Cacheable(
      value = "divisions",
      key = "#a0 + '_' + #a1 + '_' + (#a2 != null ? #a2.toString() : 'null')",
      unless = "#result.size() > 1000")
  public List<Division> getAll(DivisionFilter filter, Pagination pagination, List<Sorting> sorts) {
    log.info("Get all divisions: filter={}, pagination={}, sorts={}", filter, pagination, sorts);
    var sort = generateSort(sorts, availableSortFields);
    var pageable = generatePageable(pagination, sort);
    var spec = generateDivisionSpec(filter);
    return divisionRepository.findAll(spec, pageable, sort);
  }

  @Cacheable(value = "divisionCount", key = "(#a0 != null ? #a0 : 'null')", sync = true)
  public long getAllCount(DivisionFilter filter) {
    log.info("Get all divisions count: filter={}", filter);
    var spec = generateDivisionSpec(filter);
    return divisionRepository.count(spec);
  }

  @Cacheable(value = "divisionByName", key = "#a0", sync = true)
  public Division getByName(@NonNull String name) {
    log.info("Get division by name: name={}", name);
    return divisionRepository.findByName(name).orElse(null);
  }

  @Transactional
  @Caching(
      put = @CachePut(value = "divisionByName", key = "#a0.name"),
      evict = {
        @CacheEvict(value = "divisions", allEntries = true),
        @CacheEvict(value = "divisionCount", allEntries = true)
      })
  public Division create(@Valid @NonNull DivisionInput divisionInput) {
    log.info("Create new division: input={}", divisionInput);
    if (divisionRepository.existsByName(divisionInput.getName())) {
      throw new DivisionAlreadyExistsException();
    }

    var division = divisionMapper.toEntity(divisionInput);
    division.setArmies(armyRepository.findByNameIn(divisionInput.getArmies()));
    division.setUnits(unitRepository.findByNameIn(divisionInput.getUnits()));

    return divisionRepository.save(division);
  }

  @Transactional
  @Caching(
      put = @CachePut(value = "divisionByName", key = "#a0"),
      evict = {
        @CacheEvict(value = "divisions", allEntries = true),
        @CacheEvict(value = "divisionCount", allEntries = true)
      })
  public Division update(@NonNull String name, @Valid @NonNull DivisionInput divisionInput) {
    log.info("Update division: name={}, input={}", name, divisionInput);
    var division = divisionRepository.findByName(name).orElseThrow(DivisionNotFoundException::new);
    if (!name.equals(divisionInput.getName())
        && divisionRepository.existsByName(divisionInput.getName())) {
      throw new DivisionAlreadyExistsException();
    }

    divisionMapper.partialUpdate(divisionInput, division);
    division.setArmies(armyRepository.findByNameIn(divisionInput.getArmies()));
    division.setUnits(unitRepository.findByNameIn(divisionInput.getUnits()));

    return divisionRepository.save(division);
  }

  @Transactional
  @Caching(
      evict = {
        @CacheEvict(value = "divisions", allEntries = true),
        @CacheEvict(value = "divisionCount", allEntries = true),
        @CacheEvict(value = "divisionByName", key = "#a0")
      })
  public long delete(@NonNull String name) {
    log.info("Delete division: name={}", name);
    return divisionRepository.deleteByName(name);
  }

  @Override
  @Cacheable(value = "reference", key = "#a0", sync = true)
  public Division resolveReference(@NonNull Map<String, Object> reference) {
    log.info("Resolve reference: reference={}", reference);
    if (reference.get("name") instanceof String name) {
      return getByName(name);
    }
    return null;
  }
}
