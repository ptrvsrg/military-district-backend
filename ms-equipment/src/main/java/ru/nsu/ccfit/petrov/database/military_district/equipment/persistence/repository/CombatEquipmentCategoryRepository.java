package ru.nsu.ccfit.petrov.database.military_district.equipment.persistence.repository;

import java.util.Optional;
import ru.nsu.ccfit.petrov.database.military_district.equipment.persistence.entity.CombatEquipmentCategory;

public interface CombatEquipmentCategoryRepository
    extends SpecPageSortRepository<CombatEquipmentCategory, Integer> {

  Optional<CombatEquipmentCategory> findByName(String name);

  boolean existsByName(String name);

  long deleteByName(String name);
}
