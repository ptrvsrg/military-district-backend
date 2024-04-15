package ru.nsu.ccfit.petrov.database.military_district.weapon.persistence.repository;

import jakarta.persistence.QueryHint;
import java.util.Optional;
import org.springframework.data.jpa.repository.QueryHints;
import ru.nsu.ccfit.petrov.database.military_district.weapon.persistence.entity.WeaponCategory;

public interface WeaponCategoryRepository extends SpecPageSortRepository<WeaponCategory, Integer> {

  @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
  Optional<WeaponCategory> findByName(String name);

  @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
  boolean existsByName(String name);

  long deleteByName(String name);
}
