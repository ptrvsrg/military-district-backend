package ru.nsu.ccfit.petrov.database.military_district.formation.service;

import static ru.nsu.ccfit.petrov.database.military_district.formation.util.SpecPageSortUtils.generatePageable;
import static ru.nsu.ccfit.petrov.database.military_district.formation.util.SpecPageSortUtils.generatePlatoonSpec;
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
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.Pagination;
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.PlatoonFilter;
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.PlatoonInput;
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.Sorting;
import ru.nsu.ccfit.petrov.database.military_district.formation.exception.CompanyNotFoundException;
import ru.nsu.ccfit.petrov.database.military_district.formation.exception.PlatoonAlreadyExistsException;
import ru.nsu.ccfit.petrov.database.military_district.formation.exception.PlatoonNotFoundException;
import ru.nsu.ccfit.petrov.database.military_district.formation.mapper.PlatoonMapper;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.entity.Platoon;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.repository.CompanyRepository;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.repository.PlatoonRepository;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.repository.SquadRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class PlatoonService implements GraphQLService {

  private static final List<String> availableSortFields =
      List.of("commander.mbn", "name", "company.name");

  private final PlatoonRepository platoonRepository;
  private final CompanyRepository companyRepository;
  private final PlatoonMapper platoonMapper;

  @Cacheable(
      value = "platoons",
      key = "#a0 + '_' + #a1 + '_' + (#a2 != null ? #a2.toString() : 'null')",
      sync = true)
  public List<Platoon> getAll(PlatoonFilter filter, Pagination pagination, List<Sorting> sorts) {
    log.info("Get all platoons: filter={}, pagination={}, sorts={}", filter, pagination, sorts);
    var sort = generateSort(sorts, availableSortFields);
    var pageable = generatePageable(pagination, sort);
    var spec = generatePlatoonSpec(filter);
    return platoonRepository.findAll(spec, pageable, sort);
  }

  @Cacheable(value = "platoonCount", key = "(#a0 != null ? #a0 : 'null')", sync = true)
  public long getAllCount(PlatoonFilter filter) {
    log.info("Get all platoons count: filter={}", filter);
    var spec = generatePlatoonSpec(filter);
    return platoonRepository.count(spec);
  }

  @Cacheable(value = "platoonByName", key = "#a0", sync = true)
  public Platoon getByName(@NonNull String name) {
    log.info("Get platoon by name: {}", name);
    return platoonRepository.findByName(name).orElse(null);
  }

  @Transactional
  @Caching(
      put = @CachePut(value = "platoonByName", key = "#a0.name"),
      evict = {
        @CacheEvict(value = "platoons", allEntries = true),
        @CacheEvict(value = "platoonCount", allEntries = true)
      })
  public Platoon create(@Valid @NonNull PlatoonInput platoonInput) {
    log.info("Create platoon: input={}", platoonInput);
    if (platoonRepository.existsByName(platoonInput.getName())) {
      throw new PlatoonAlreadyExistsException();
    }

    var platoon = platoonMapper.toEntity(platoonInput);
    platoon.setCompany(
        companyRepository
            .findByName(platoonInput.getCompany())
            .orElseThrow(CompanyNotFoundException::new));

    return platoonRepository.save(platoon);
  }

  @Transactional
  @Caching(
      put = @CachePut(value = "platoonByName", key = "#a0"),
      evict = {
        @CacheEvict(value = "platoons", allEntries = true),
        @CacheEvict(value = "platoonCount", allEntries = true)
      })
  public Platoon update(@NonNull String name, @Valid @NonNull PlatoonInput platoonInput) {
    log.info("Update platoon: name={}, input={}", name, platoonInput);
    var platoon = platoonRepository.findByName(name).orElseThrow(PlatoonNotFoundException::new);
    if (!name.equals(platoonInput.getName())
        && platoonRepository.existsByName(platoonInput.getName())) {
      throw new PlatoonAlreadyExistsException();
    }

    platoonMapper.partialUpdate(platoonInput, platoon);
    platoon.setCompany(
        companyRepository
            .findByName(platoonInput.getCompany())
            .orElseThrow(CompanyNotFoundException::new));

    return platoonRepository.save(platoon);
  }

  @Transactional
  @Caching(
      evict = {
        @CacheEvict(value = "platoons", allEntries = true),
        @CacheEvict(value = "platoonCount", allEntries = true),
        @CacheEvict(value = "platoonByName", key = "#a0")
      })
  public long delete(@NonNull String name) {
    log.info("Delete platoon: name={}", name);
    return platoonRepository.deleteByName(name);
  }

  @Override
  @Cacheable(value = "reference", key = "#a0", sync = true)
  public Platoon resolveReference(@NonNull Map<String, Object> reference) {
    log.info("Resolve reference: reference={}", reference);
    if (reference.get("name") instanceof String name) {
      return getByName(name);
    }
    return null;
  }
}
