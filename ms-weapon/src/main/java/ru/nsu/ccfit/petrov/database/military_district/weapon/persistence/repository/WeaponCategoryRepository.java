package ru.nsu.ccfit.petrov.database.military_district.weapon.persistence.repository;

import java.util.Optional;
import ru.nsu.ccfit.petrov.database.military_district.weapon.persistence.entity.WeaponCategory;

public interface WeaponCategoryRepository extends SpecPageSortRepository<WeaponCategory, Integer> {

  Optional<WeaponCategory> findByName(String name);

  boolean existsByName(String name);

  long deleteByName(String name);
}
