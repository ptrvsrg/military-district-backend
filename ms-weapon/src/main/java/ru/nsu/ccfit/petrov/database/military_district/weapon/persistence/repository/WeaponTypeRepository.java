package ru.nsu.ccfit.petrov.database.military_district.weapon.persistence.repository;

import java.util.Optional;
import ru.nsu.ccfit.petrov.database.military_district.weapon.persistence.entity.WeaponType;

public interface WeaponTypeRepository extends SpecPageSortRepository<WeaponType, Integer> {

  Optional<WeaponType> findByName(String name);

  Optional<WeaponType> findByNameAndCategory_Name(String name, String name1);

  boolean existsByNameAndCategory_Name(String name, String name1);

  long deleteByNameAndCategory_Name(String name, String category);
}
