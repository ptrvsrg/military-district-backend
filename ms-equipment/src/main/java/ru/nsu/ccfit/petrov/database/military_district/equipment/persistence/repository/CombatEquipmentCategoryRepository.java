package ru.nsu.ccfit.petrov.database.military_district.equipment.persistence.repository;

import jakarta.persistence.QueryHint;
import java.util.Optional;
import org.springframework.data.jpa.repository.QueryHints;
import ru.nsu.ccfit.petrov.database.military_district.equipment.persistence.entity.CombatEquipmentCategory;

public interface CombatEquipmentCategoryRepository
    extends SpecPageSortRepository<CombatEquipmentCategory, Integer> {

  @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
  Optional<CombatEquipmentCategory> findByName(String name);

  @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
  boolean existsByName(String name);

  long deleteByName(String name);
}
