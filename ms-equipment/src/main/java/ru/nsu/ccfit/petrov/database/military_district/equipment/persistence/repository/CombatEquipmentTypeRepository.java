package ru.nsu.ccfit.petrov.database.military_district.equipment.persistence.repository;

import java.util.Optional;
import ru.nsu.ccfit.petrov.database.military_district.equipment.persistence.entity.CombatEquipmentType;

public interface CombatEquipmentTypeRepository
    extends SpecPageSortRepository<CombatEquipmentType, Integer> {

  Optional<CombatEquipmentType> findByName(String name);

  Optional<CombatEquipmentType> findByNameAndCategory_Name(String name, String name1);

  boolean existsByNameAndCategory_Name(String name, String name1);

  long deleteByNameAndCategory_Name(String name, String category);
}
