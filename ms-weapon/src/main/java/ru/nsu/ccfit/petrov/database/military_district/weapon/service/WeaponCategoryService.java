package ru.nsu.ccfit.petrov.database.military_district.weapon.service;

import static ru.nsu.ccfit.petrov.database.military_district.weapon.util.SpecPageSortUtils.generatePageable;
import static ru.nsu.ccfit.petrov.database.military_district.weapon.util.SpecPageSortUtils.generateSort;

import java.util.List;
import java.util.Map;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.ccfit.petrov.database.military_district.weapon.exception.WeaponCategoryAlreadyExistsException;
import ru.nsu.ccfit.petrov.database.military_district.weapon.exception.WeaponCategoryNotFoundException;
import ru.nsu.ccfit.petrov.database.military_district.weapon.persistence.entity.WeaponCategory;
import ru.nsu.ccfit.petrov.database.military_district.weapon.persistence.repository.WeaponCategoryRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WeaponCategoryService implements GraphQLService {

  private static final List<String> availableSortFields = List.of("name");

  private final WeaponCategoryRepository weaponCategoryRepository;

  public List<WeaponCategory> getAll(
      Integer page, Integer pageSize, String sortField, Boolean sortAsc) {
    var sort = generateSort(sortField, sortAsc, availableSortFields);
    var pageable = generatePageable(page, pageSize, sort);
    return weaponCategoryRepository.findAll(null, pageable, sort);
  }

  public long getAllCount() {
    return weaponCategoryRepository.count();
  }

  public WeaponCategory getByName(@NonNull String name) {
    return weaponCategoryRepository.findByName(name).orElse(null);
  }

  @Transactional
  public WeaponCategory create(@NonNull String category) {
    if (weaponCategoryRepository.existsByName(category)) {
      throw new WeaponCategoryAlreadyExistsException();
    }

    var entity = new WeaponCategory();
    entity.setName(category);

    return weaponCategoryRepository.save(entity);
  }

  @Transactional
  public WeaponCategory update(@NonNull String name, @NonNull String category) {
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
    return weaponCategoryRepository.deleteByName(name);
  }

  @Override
  public WeaponCategory resolveReference(@NonNull Map<String, Object> reference) {
    if (reference.get("name") instanceof String name) {
      return getByName(name);
    }
    return null;
  }
}
