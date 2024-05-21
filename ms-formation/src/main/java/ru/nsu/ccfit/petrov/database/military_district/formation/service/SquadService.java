package ru.nsu.ccfit.petrov.database.military_district.formation.service;

import static ru.nsu.ccfit.petrov.database.military_district.formation.util.SpecPageSortUtils.generatePageable;
import static ru.nsu.ccfit.petrov.database.military_district.formation.util.SpecPageSortUtils.generateSort;
import static ru.nsu.ccfit.petrov.database.military_district.formation.util.SpecPageSortUtils.generateSquadSpec;

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
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.SquadFilter;
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.SquadInput;
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
@Slf4j
public class SquadService implements GraphQLService {

  private static final List<String> availableSortFields =
      List.of("commander.mbn", "name", "platoon.name");

  private final SquadRepository squadRepository;
  private final PlatoonRepository platoonRepository;
  private final SquadMapper squadMapper;

  @Cacheable(
      value = "squads",
      key = "#a0 + '_' + #a1 + '_' + (#a2 != null ? #a2.toString() : 'null')",
      unless = "#result.size() > 1000")
  public List<Squad> getAll(SquadFilter filter, Pagination pagination, List<Sorting> sorts) {
    log.info("Get all squads: filter={}, pagination={}, sorts={}", filter, pagination, sorts);
    var sort = generateSort(sorts, availableSortFields);
    var pageable = generatePageable(pagination, sort);
    var spec = generateSquadSpec(filter);
    return squadRepository.findAll(spec, pageable, sort);
  }

  @Cacheable(value = "squadCount", key = "(#a0 != null ? #a0 : 'null')", sync = true)
  public long getAllCount(SquadFilter filter) {
    log.info("Get all squads count: filter={}", filter);
    var spec = generateSquadSpec(filter);
    return squadRepository.count(spec);
  }

  @Cacheable(value = "squadByName", key = "#a0", sync = true)
  public Squad getByName(@NonNull String name) {
    log.info("Get squad by name: name={}", name);
    return squadRepository.findByName(name).orElse(null);
  }

  @Transactional
  @Caching(
      put = @CachePut(value = "squadByName", key = "#a0.name"),
      evict = {
        @CacheEvict(value = "squads", allEntries = true),
        @CacheEvict(value = "squadCount", allEntries = true)
      })
  public Squad create(@Valid @NonNull SquadInput squadInput) {
    log.info("Create squad: input={}", squadInput);
    if (squadRepository.existsByName(squadInput.getName())) {
      throw new SquadAlreadyExistsException();
    }

    var squad = squadMapper.toEntity(squadInput);
    squad.setPlatoon(
        platoonRepository
            .findByName(squadInput.getPlatoon())
            .orElseThrow(PlatoonNotFoundException::new));

    return squadRepository.save(squad);
  }

  @Transactional
  @Caching(
      put = @CachePut(value = "squadByName", key = "#a0"),
      evict = {
        @CacheEvict(value = "squads", allEntries = true),
        @CacheEvict(value = "squadCount", allEntries = true)
      })
  public Squad update(@NonNull String name, @Valid @NonNull SquadInput squadInput) {
    log.info("Update squad: name={}, input={}", name, squadInput);
    var squad = squadRepository.findByName(name).orElseThrow(SquadNotFoundException::new);
    if (!name.equals(squadInput.getName()) && squadRepository.existsByName(squadInput.getName())) {
      throw new SquadAlreadyExistsException();
    }

    squadMapper.partialUpdate(squadInput, squad);
    squad.setPlatoon(
        platoonRepository
            .findByName(squadInput.getPlatoon())
            .orElseThrow(PlatoonNotFoundException::new));

    return squadRepository.save(squad);
  }

  @Transactional
  @Caching(
      evict = {
        @CacheEvict(value = "squads", allEntries = true),
        @CacheEvict(value = "squadCount", allEntries = true),
        @CacheEvict(value = "squadByName", key = "#a0")
      })
  public long delete(@NonNull String name) {
    log.info("Delete squad: name={}", name);
    return squadRepository.deleteByName(name);
  }

  @Override
  @Cacheable(value = "reference", key = "#a0", sync = true)
  public Squad resolveReference(@NonNull Map<String, Object> reference) {
    log.info("Resolve reference: reference={}", reference);
    if (reference.get("name") instanceof String name) {
      return getByName(name);
    }
    return null;
  }
}
