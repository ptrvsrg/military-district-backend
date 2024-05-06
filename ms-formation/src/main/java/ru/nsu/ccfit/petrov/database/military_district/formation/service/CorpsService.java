package ru.nsu.ccfit.petrov.database.military_district.formation.service;

import static ru.nsu.ccfit.petrov.database.military_district.formation.util.SpecPageSortUtils.generateCorpsSpec;
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
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.CorpsFilter;
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.CorpsInput;
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.Pagination;
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.Sorting;
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
@Slf4j
public class CorpsService implements GraphQLService {

  private static final List<String> availableSortFields = List.of("commander.mbn", "name");

  private final CorpsRepository corpsRepository;
  private final ArmyRepository armyRepository;
  private final UnitRepository unitRepository;
  private final CorpsMapper corpsMapper;

  @Cacheable(value = "corps", key = "#a0 + '_' + #a1 + '_' + #a2", sync = true)
  public List<Corps> getAll(CorpsFilter filter, Pagination pagination, List<Sorting> sorts) {
    log.info("Get all corps: filter={}, pagination={}, sorts={}", filter, pagination, sorts);
    var sort = generateSort(sorts, availableSortFields);
    var pageable = generatePageable(pagination, sort);
    var spec = generateCorpsSpec(filter);
    return corpsRepository.findAll(spec, pageable, sort);
  }

  @Cacheable(value = "corpsCount", key = "'filter_' + #a0", sync = true)
  public long getAllCount(CorpsFilter filter) {
    log.info("Get all corps count: filter={}", filter);
    var spec = generateCorpsSpec(filter);
    return corpsRepository.count(spec);
  }

  @Cacheable(value = "corpsByName", key = "#a0", sync = true)
  public Corps getByName(@NonNull String name) {
    log.info("Get corps: name={}", name);
    return corpsRepository.findByName(name).orElse(null);
  }

  @Transactional
  @Caching(
      put = @CachePut(value = "corpsByName", key = "#a0.name"),
      evict = {
        @CacheEvict(value = "corps", allEntries = true),
        @CacheEvict(value = "corpsCount", allEntries = true)
      })
  public Corps create(@Valid @NonNull CorpsInput corpsInput) {
    log.info("Create corps: input={}", corpsInput);
    if (corpsRepository.existsByName(corpsInput.getName())) {
      throw new CorpsAlreadyExistsException();
    }

    var corps = corpsMapper.toEntity(corpsInput);
    corps.setArmies(armyRepository.findByNameIn(corpsInput.getArmies()));
    corps.setUnits(unitRepository.findByNameIn(corpsInput.getUnits()));

    return corpsRepository.save(corps);
  }

  @Transactional
  @Caching(
      put = @CachePut(value = "corpsByName", key = "#a0"),
      evict = {
        @CacheEvict(value = "corps", allEntries = true),
        @CacheEvict(value = "corpsCount", allEntries = true)
      })
  public Corps update(@NonNull String name, @Valid @NonNull CorpsInput corpsInput) {
    log.info("Update corps: name={}, input={}", name, corpsInput);
    var corps = corpsRepository.findByName(name).orElseThrow(CorpsNotFoundException::new);
    if (!name.equals(corpsInput.getName()) && corpsRepository.existsByName(corpsInput.getName())) {
      throw new CorpsAlreadyExistsException();
    }

    corpsMapper.partialUpdate(corpsInput, corps);
    corps.setArmies(armyRepository.findByNameIn(corpsInput.getArmies()));
    corps.setUnits(unitRepository.findByNameIn(corpsInput.getUnits()));

    return corpsRepository.save(corps);
  }

  @Transactional
  @Caching(
      evict = {
        @CacheEvict(value = "corps", allEntries = true),
        @CacheEvict(value = "corpsCount", allEntries = true),
        @CacheEvict(value = "corpsByName", key = "#a0")
      })
  public long delete(@NonNull String name) {
    log.info("Delete corps: name={}", name);
    return corpsRepository.deleteByName(name);
  }

  @Override
  @Cacheable(value = "reference", key = "#a0", sync = true)
  public Corps resolveReference(@NonNull Map<String, Object> reference) {
    log.info("Resolve reference: reference={}", reference);
    if (reference.get("name") instanceof String name) {
      return getByName(name);
    }
    return null;
  }
}
