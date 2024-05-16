package ru.nsu.ccfit.petrov.database.military_district.formation.service;

import static ru.nsu.ccfit.petrov.database.military_district.formation.util.SpecPageSortUtils.generatePageable;
import static ru.nsu.ccfit.petrov.database.military_district.formation.util.SpecPageSortUtils.generateSort;
import static ru.nsu.ccfit.petrov.database.military_district.formation.util.SpecPageSortUtils.generateUnitSpec;

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
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.Pagination;
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.Sorting;
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.UnitFilter;
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.UnitInput;
import ru.nsu.ccfit.petrov.database.military_district.formation.exception.UnitAlreadyExistsException;
import ru.nsu.ccfit.petrov.database.military_district.formation.exception.UnitNotFoundException;
import ru.nsu.ccfit.petrov.database.military_district.formation.mapper.UnitMapper;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.entity.Unit;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.repository.BrigadeRepository;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.repository.CorpsRepository;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.repository.DivisionRepository;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.repository.UnitRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UnitService implements GraphQLService {

  private static final List<String> availableSortFields =
      List.of("name", "commander.mbn", "address");

  private final UnitRepository unitRepository;
  private final BrigadeRepository brigadeRepository;
  private final CorpsRepository corpsRepository;
  private final DivisionRepository divisionRepository;
  private final UnitMapper unitMapper;

  @Cacheable(
      value = "units",
      key = "#a0 + '_' + #a1 + '_' + (#a2 != null ? #a2.toString() : 'null')",
      unless = "#result.size() > 1000")
  public List<Unit> getAll(UnitFilter filter, Pagination pagination, List<Sorting> sorts) {
    log.info("Get all units: filter={}, pagination={}, sorts={}", filter, pagination, sorts);
    var sort = generateSort(sorts, availableSortFields);
    var pageable = generatePageable(pagination, sort);
    var spec = generateUnitSpec(filter);
    return unitRepository.findAll(spec, pageable, sort);
  }

  @Cacheable(value = "unitCount", key = "(#a0 != null ? #a0 : 'null')", sync = true)
  public long getAllCount(UnitFilter filter) {
    log.info("Get all units count: filter={}", filter);
    var spec = generateUnitSpec(filter);
    return unitRepository.count(spec);
  }

  @Cacheable(value = "unitByName", key = "#a0", sync = true)
  public Unit getByName(@NonNull String name) {
    log.info("Get unit by name: name={}", name);
    return unitRepository.findByName(name).orElse(null);
  }

  @Transactional
  @Caching(
      put = @CachePut(value = "unitByName", key = "#a0.name"),
      evict = {
        @CacheEvict(value = "units", allEntries = true),
        @CacheEvict(value = "unitCount", allEntries = true)
      })
  public Unit create(@Valid @NonNull UnitInput unitInput) {
    log.info("Create unit: input={}", unitInput);
    if (unitRepository.existsByName(unitInput.getName())) {
      throw new UnitAlreadyExistsException();
    }

    var unit = unitMapper.toEntity(unitInput);
    unit.setBrigades(brigadeRepository.findByNameIn(unitInput.getBrigades()));
    unit.setCorps(corpsRepository.findByNameIn(unitInput.getCorps()));
    unit.setDivisions(divisionRepository.findByNameIn(unitInput.getDivisions()));

    return unitRepository.save(unit);
  }

  @Transactional
  @Caching(
      put = @CachePut(value = "unitByName", key = "#a0"),
      evict = {
        @CacheEvict(value = "units", allEntries = true),
        @CacheEvict(value = "unitCount", allEntries = true)
      })
  public Unit update(@NonNull String name, @Valid @NonNull UnitInput unitInput) {
    log.info("Update unit: name={}, input={}", name, unitInput);
    var unit = unitRepository.findByName(name).orElseThrow(UnitNotFoundException::new);
    if (!name.equals(unitInput.getName()) && unitRepository.existsByName(unitInput.getName())) {
      throw new UnitAlreadyExistsException();
    }

    unitMapper.partialUpdate(unitInput, unit);
    unit.setBrigades(brigadeRepository.findByNameIn(unitInput.getBrigades()));
    unit.setCorps(corpsRepository.findByNameIn(unitInput.getCorps()));
    unit.setDivisions(divisionRepository.findByNameIn(unitInput.getDivisions()));

    return unitRepository.save(unit);
  }

  @Transactional
  @Caching(
      evict = {
        @CacheEvict(value = "units", allEntries = true),
        @CacheEvict(value = "unitCount", allEntries = true),
        @CacheEvict(value = "unitByName", key = "#a0")
      })
  public long delete(@NonNull String name) {
    log.info("Delete unit: name={}", name);
    return unitRepository.deleteByName(name);
  }

  @Override
  @Cacheable(value = "reference", key = "#a0", sync = true)
  public Unit resolveReference(@NonNull Map<String, Object> reference) {
    log.info("Resolve reference: reference={}", reference);
    if (reference.get("name") instanceof String name) {
      return getByName(name);
    }
    return null;
  }
}
