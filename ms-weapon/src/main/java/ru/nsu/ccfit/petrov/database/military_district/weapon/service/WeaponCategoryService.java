package ru.nsu.ccfit.petrov.database.military_district.weapon.service;

import static ru.nsu.ccfit.petrov.database.military_district.weapon.util.SpecPageSortUtils.generatePageable;
import static ru.nsu.ccfit.petrov.database.military_district.weapon.util.SpecPageSortUtils.generateSort;

import java.util.List;
import java.util.Map;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
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

  @Cacheable("weaponCategories")
  public List<WeaponCategory> getAll(Pagination pagination, List<Sorting> sorts) {
    log.info("Get all weapon categories: pagination={}, sorts={}", pagination, sorts);
    var sort = generateSort(sorts, availableSortFields);
    var pageable = generatePageable(pagination, sort);
    return weaponCategoryRepository.findAll(null, pageable, sort);
  }

  @Cacheable("weaponCategoryCount")
  public long getAllCount() {
    log.info("Get all weapon categories count");
    return weaponCategoryRepository.count();
  }

  @Cacheable("weaponCategoryByName")
  public WeaponCategory getByName(@NonNull String name) {
    log.info("Get weapon category by name: name={}", name);
    return weaponCategoryRepository.findByName(name).orElse(null);
  }

  @Transactional
  public WeaponCategory create(@NonNull String category) {
    log.info("Create weapon category: input={}", category);
    if (weaponCategoryRepository.existsByName(category)) {
      throw new WeaponCategoryAlreadyExistsException();
    }

    var entity = new WeaponCategory();
    entity.setName(category);

    return weaponCategoryRepository.save(entity);
  }

  @Transactional
  public WeaponCategory update(@NonNull String name, @NonNull String category) {
    log.info("Update weapon category: name={}, input={}", name, category);
    var entity =
        weaponCategoryRepository.findByName(name).orElseThrow(WeaponCategoryNotFoundException::new);

    if (!name.equals(category) && weaponCategoryRepository.existsByName(category)) {
      throw new WeaponCategoryAlreadyExistsException();
    }

    entity.setName(category);
    return weaponCategoryRepository.save(entity);
  }

  @Transactional
  public long delete(@NonNull String name) {
    log.info("Delete weapon category: name={}", name);
    return weaponCategoryRepository.deleteByName(name);
  }

  @Override
  public WeaponCategory resolveReference(@NonNull Map<String, Object> reference) {
    log.info("Resolve reference: reference={}", reference);
    if (reference.get("name") instanceof String name) {
      return getByName(name);
    }
    return null;
  }
}
