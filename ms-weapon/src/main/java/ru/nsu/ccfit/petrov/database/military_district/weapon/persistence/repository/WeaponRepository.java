package ru.nsu.ccfit.petrov.database.military_district.weapon.persistence.repository;

import jakarta.persistence.QueryHint;
import java.util.Optional;
import org.springframework.data.jpa.repository.QueryHints;
import ru.nsu.ccfit.petrov.database.military_district.weapon.persistence.entity.Weapon;

public interface WeaponRepository extends SpecPageSortRepository<Weapon, Integer> {

  @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
  Optional<Weapon> findBySerialNumber(String serialNumber);

  @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
  boolean existsBySerialNumber(String serialNumber);

  long deleteBySerialNumber(String serialNumber);
}
