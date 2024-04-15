package ru.nsu.ccfit.petrov.database.military_district.weapon.persistence.repository;

import jakarta.persistence.QueryHint;
import java.util.Optional;
import org.springframework.data.jpa.repository.QueryHints;
import ru.nsu.ccfit.petrov.database.military_district.weapon.persistence.entity.WeaponType;

public interface WeaponTypeRepository extends SpecPageSortRepository<WeaponType, Integer> {

  @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
  Optional<WeaponType> findByName(String name);

  @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
  Optional<WeaponType> findByNameAndCategory_Name(String name, String name1);

  @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
  boolean existsByNameAndCategory_Name(String name, String name1);

  long deleteByNameAndCategory_Name(String name, String category);
}
