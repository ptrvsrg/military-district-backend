package ru.nsu.ccfit.petrov.database.military_district.equipment.service;

import static ru.nsu.ccfit.petrov.database.military_district.equipment.util.SpecPageSortUtils.generatePageable;
import static ru.nsu.ccfit.petrov.database.military_district.equipment.util.SpecPageSortUtils.generateSort;

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
import ru.nsu.ccfit.petrov.database.military_district.equipment.dto.Pagination;
import ru.nsu.ccfit.petrov.database.military_district.equipment.dto.Sorting;
import ru.nsu.ccfit.petrov.database.military_district.equipment.exception.CombatEquipmentCategoryAlreadyExistsException;
import ru.nsu.ccfit.petrov.database.military_district.equipment.exception.CombatEquipmentCategoryNotFoundException;
import ru.nsu.ccfit.petrov.database.military_district.equipment.persistence.entity.CombatEquipmentCategory;
import ru.nsu.ccfit.petrov.database.military_district.equipment.persistence.repository.CombatEquipmentCategoryRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CombatEquipmentCategoryService implements GraphQLService {

  private static final List<String> availableSortFields = List.of("name");

  private final CombatEquipmentCategoryRepository combatEquipmentCategoryRepository;

  @Cacheable(
      value = "combatEquipmentCategories",
      key = "#a0 + '_' + (#a1 != null ? #a1.toString() : 'null')",
      unless = "#result.size() > 1000",
      sync = true)
  public List<CombatEquipmentCategory> getAll(Pagination pagination, List<Sorting> sorts) {
    log.info("Get all combat equipment categories: pagination={}, sorts={}", pagination, sorts);
    var sort = generateSort(sorts, availableSortFields);
    var pageable = generatePageable(pagination, sort);
    return combatEquipmentCategoryRepository.findAll(null, pageable, sort);
  }

  @Cacheable(value = "combatEquipmentCategoryCount", sync = true)
  public long getAllCount() {
    log.info("Get all combat equipment category count");
    return combatEquipmentCategoryRepository.count();
  }

  @Cacheable(value = "combatEquipmentCategoryByName", key = "#a0", sync = true)
  public CombatEquipmentCategory getByName(@NonNull String name) {
    log.info("Get combat equipment category by name: name={}", name);
    return combatEquipmentCategoryRepository.findByName(name).orElse(null);
  }

  @Transactional
  @Caching(
      put = @CachePut(value = "combatEquipmentCategoryByName", key = "#a0"),
      evict = {
        @CacheEvict(value = "combatEquipmentCategories", allEntries = true),
        @CacheEvict(value = "combatEquipmentCategoryCount", allEntries = true)
      })
  public CombatEquipmentCategory create(@NonNull String category) {
    log.info("Create combat equipment category: input={}", category);
    if (combatEquipmentCategoryRepository.existsByName(category)) {
      throw new CombatEquipmentCategoryAlreadyExistsException();
    }

    var entity = new CombatEquipmentCategory();
    entity.setName(category);

    return combatEquipmentCategoryRepository.save(entity);
  }

  @Transactional
  @Caching(
      put = @CachePut(value = "combatEquipmentCategoryByName", key = "#a0"),
      evict = {
        @CacheEvict(value = "combatEquipmentCategories", allEntries = true),
        @CacheEvict(value = "combatEquipmentCategoryCount", allEntries = true)
      })
  public CombatEquipmentCategory update(@NonNull String name, @NonNull String category) {
    log.info("Update combat equipment category: name={}, input={}", name, category);
    var entity =
        combatEquipmentCategoryRepository
            .findByName(name)
            .orElseThrow(CombatEquipmentCategoryNotFoundException::new);

    if (!name.equals(category) && combatEquipmentCategoryRepository.existsByName(category)) {
      throw new CombatEquipmentCategoryAlreadyExistsException();
    }

    entity.setName(category);
    return combatEquipmentCategoryRepository.save(entity);
  }

  @Transactional
  @Caching(
      evict = {
        @CacheEvict(value = "combatEquipmentCategories", allEntries = true),
        @CacheEvict(value = "combatEquipmentCategoryCount", allEntries = true),
        @CacheEvict(value = "combatEquipmentCategoryByName", key = "#a0")
      })
  public long delete(@NonNull String name) {
    log.info("Delete combat equipment category: name={}", name);
    return combatEquipmentCategoryRepository.deleteByName(name);
  }

  @Override
  @Cacheable(value = "reference", key = "#a0", sync = true)
  public CombatEquipmentCategory resolveReference(@NonNull Map<String, Object> reference) {
    log.info("Resolve combat equipment category reference: reference={}", reference);
    if (reference.get("name") instanceof String name) {
      return combatEquipmentCategoryRepository.findByName(name).orElse(null);
    }
    return null;
  }
}
