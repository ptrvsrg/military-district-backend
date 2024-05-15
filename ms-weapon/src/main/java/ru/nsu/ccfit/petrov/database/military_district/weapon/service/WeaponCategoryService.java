package ru.nsu.ccfit.petrov.database.military_district.weapon.service;

import static ru.nsu.ccfit.petrov.database.military_district.weapon.util.SpecPageSortUtils.generatePageable;
import static ru.nsu.ccfit.petrov.database.military_district.weapon.util.SpecPageSortUtils.generateSort;

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
import ru.nsu.ccfit.petrov.database.military_district.weapon.dto.Pagination;
import ru.nsu.ccfit.petrov.database.military_district.weapon.dto.Sorting;
import ru.nsu.ccfit.petrov.database.military_district.weapon.exception.WeaponCategoryAlreadyExistsException;
import ru.nsu.ccfit.petrov.database.military_district.weapon.exception.WeaponCategoryNotFoundException;
import ru.nsu.ccfit.petrov.database.military_district.weapon.persistence.entity.WeaponCategory;
import ru.nsu.ccfit.petrov.database.military_district.weapon.persistence.repository.WeaponCategoryRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class WeaponCategoryService implements GraphQLService {

  private static final List<String> availableSortFields = List.of("name");

  private final WeaponCategoryRepository weaponCategoryRepository;

  @Cacheable(
      value = "weaponCategories",
      key = "#a0 + '_' + (#a1 != null ? #a1.toString() : 'null')",
      unless = "#result.size() > 1000",
      sync = true)
  public List<WeaponCategory> getAll(Pagination pagination, List<Sorting> sorts) {
    log.info("Get all combat weapon categories: pagination={}, sorts={}", pagination, sorts);
    var sort = generateSort(sorts, availableSortFields);
    var pageable = generatePageable(pagination, sort);
    return weaponCategoryRepository.findAll(null, pageable, sort);
  }

  @Cacheable(value = "weaponCategoryCount", sync = true)
  public long getAllCount() {
    log.info("Get all combat weapon category count");
    return weaponCategoryRepository.count();
  }

  @Cacheable(value = "weaponCategoryByName", key = "#a0", sync = true)
  public WeaponCategory getByName(@NonNull String name) {
    log.info("Get combat weapon category by name: name={}", name);
    return weaponCategoryRepository.findByName(name).orElse(null);
  }

  @Transactional
  @Caching(
      put = @CachePut(value = "weaponCategoryByName", key = "#a0"),
      evict = {
        @CacheEvict(value = "weaponCategories", allEntries = true),
        @CacheEvict(value = "weaponCategoryCount", allEntries = true)
      })
  public WeaponCategory create(@NonNull String category) {
    log.info("Create combat weapon category: input={}", category);
    if (weaponCategoryRepository.existsByName(category)) {
      throw new WeaponCategoryAlreadyExistsException();
    }

    var entity = new WeaponCategory();
    entity.setName(category);

    return weaponCategoryRepository.save(entity);
  }

  @Transactional
  @Caching(
      put = @CachePut(value = "weaponCategoryByName", key = "#a0"),
      evict = {
        @CacheEvict(value = "weaponCategories", allEntries = true),
        @CacheEvict(value = "weaponCategoryCount", allEntries = true)
      })
  public WeaponCategory update(@NonNull String name, @NonNull String category) {
    log.info("Update combat weapon category: name={}, input={}", name, category);
    var entity =
        weaponCategoryRepository.findByName(name).orElseThrow(WeaponCategoryNotFoundException::new);

    if (!name.equals(category) && weaponCategoryRepository.existsByName(category)) {
      throw new WeaponCategoryAlreadyExistsException();
    }

    entity.setName(category);
    return weaponCategoryRepository.save(entity);
  }

  @Transactional
  @Caching(
      evict = {
        @CacheEvict(value = "weaponCategories", allEntries = true),
        @CacheEvict(value = "weaponCategoryCount", allEntries = true),
        @CacheEvict(value = "weaponCategoryByName", key = "#a0")
      })
  public long delete(@NonNull String name) {
    log.info("Delete combat weapon category: name={}", name);
    return weaponCategoryRepository.deleteByName(name);
  }

  @Override
  @Cacheable(value = "reference", key = "#a0", sync = true)
  public WeaponCategory resolveReference(@NonNull Map<String, Object> reference) {
    log.info("Resolve combat weapon category reference: reference={}", reference);
    if (reference.get("name") instanceof String name) {
      return weaponCategoryRepository.findByName(name).orElse(null);
    }
    return null;
  }
}
