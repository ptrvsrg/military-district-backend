package ru.nsu.ccfit.petrov.database.military_district.weapon.persistence.repository;

import java.util.Optional;
import ru.nsu.ccfit.petrov.database.military_district.weapon.persistence.entity.Weapon;

public interface WeaponRepository extends SpecPageSortRepository<Weapon, Integer> {

  Optional<Weapon> findBySerialNumber(String serialNumber);

  boolean existsBySerialNumber(String serialNumber);

  long deleteBySerialNumber(String serialNumber);
}
