package ru.nsu.ccfit.petrov.database.military_district.formation.service;

import static ru.nsu.ccfit.petrov.database.military_district.formation.util.SpecPageSortUtils.generateBrigadeSpec;
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
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.BrigadeFilter;
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.BrigadeInput;
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.Pagination;
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.Sorting;
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
@Slf4j
public class BrigadeService implements GraphQLService {

  private static final List<String> availableSortFields = List.of("commander.mbn", "name");

  private final BrigadeRepository brigadeRepository;
  private final ArmyRepository armyRepository;
  private final UnitRepository unitRepository;
  private final BrigadeMapper brigadeMapper;

  @Cacheable(
      value = "brigades",
      key = "#a0 + '_' + #a1 + '_' + (#a2 != null ? #a2.toString() : 'null')",
      sync = true)
  public List<Brigade> getAll(BrigadeFilter filter, Pagination pagination, List<Sorting> sorts) {
    log.info("Get all brigades: filter={}, pagination={}, sorts={}", filter, pagination, sorts);
    var sort = generateSort(sorts, availableSortFields);
    var pageable = generatePageable(pagination, sort);
    var spec = generateBrigadeSpec(filter);
    return brigadeRepository.findAll(spec, pageable, sort);
  }

  @Cacheable(value = "brigadeCount", key = "(#a0 != null ? #a0 : 'null')", sync = true)
  public long getAllCount(BrigadeFilter filter) {
    log.info("Get all brigades count: filter={}", filter);
    var spec = generateBrigadeSpec(filter);
    return brigadeRepository.count(spec);
  }

  @Cacheable(value = "brigadeByName", key = "#a0", sync = true)
  public Brigade getByName(@NonNull String name) {
    log.info("Get brigade: name={}", name);
    return brigadeRepository.findByName(name).orElse(null);
  }

  @Transactional
  @Caching(
      put = @CachePut(value = "brigadeByName", key = "#a0.name"),
      evict = {
        @CacheEvict(value = "brigades", allEntries = true),
        @CacheEvict(value = "brigadeCount", allEntries = true)
      })
  public Brigade create(@Valid @NonNull BrigadeInput brigadeInput) {
    log.info("Create brigade: input={}", brigadeInput);
    if (brigadeRepository.existsByName(brigadeInput.getName())) {
      throw new BrigadeAlreadyExistsException();
    }

    var brigade = brigadeMapper.toEntity(brigadeInput);
    brigade.setArmies(armyRepository.findByNameIn(brigadeInput.getArmies()));
    brigade.setUnits(unitRepository.findByNameIn(brigadeInput.getUnits()));

    return brigadeRepository.save(brigade);
  }

  @Transactional
  @Caching(
      put = @CachePut(value = "brigadeByName", key = "#a0"),
      evict = {
        @CacheEvict(value = "brigades", allEntries = true),
        @CacheEvict(value = "brigadeCount", allEntries = true)
      })
  public Brigade update(@NonNull String name, @Valid @NonNull BrigadeInput brigadeInput) {
    log.info("Update brigade: name={}, input={}", name, brigadeInput);
    var brigade = brigadeRepository.findByName(name).orElseThrow(BrigadeNotFoundException::new);
    if (!name.equals(brigadeInput.getName())
        && brigadeRepository.existsByName(brigadeInput.getName())) {
      throw new BrigadeAlreadyExistsException();
    }

    brigadeMapper.partialUpdate(brigadeInput, brigade);
    brigade.setArmies(armyRepository.findByNameIn(brigadeInput.getArmies()));
    brigade.setUnits(unitRepository.findByNameIn(brigadeInput.getUnits()));

    return brigadeRepository.save(brigade);
  }

  @Transactional
  @Caching(
      evict = {
        @CacheEvict(value = "brigades", allEntries = true),
        @CacheEvict(value = "brigadeCount", allEntries = true),
        @CacheEvict(value = "brigadeByName", key = "#a0")
      })
  public long delete(@NonNull String name) {
    log.info("Delete brigade: name={}", name);
    return brigadeRepository.deleteByName(name);
  }

  @Override
  @Cacheable(value = "reference", key = "#a0", sync = true)
  public Brigade resolveReference(@NonNull Map<String, Object> reference) {
    log.info("Resolve reference: reference={}", reference);
    if (reference.get("name") instanceof String name) {
      return brigadeRepository.findByName(name).orElse(null);
    }
    return null;
  }
}
