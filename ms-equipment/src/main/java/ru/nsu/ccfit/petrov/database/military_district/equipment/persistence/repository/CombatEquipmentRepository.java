package ru.nsu.ccfit.petrov.database.military_district.equipment.persistence.repository;

import java.util.Optional;
import ru.nsu.ccfit.petrov.database.military_district.equipment.persistence.entity.CombatEquipment;

public interface CombatEquipmentRepository
    extends SpecPageSortRepository<CombatEquipment, Integer> {

  Optional<CombatEquipment> findBySerialNumber(String serialNumber);

  boolean existsBySerialNumber(String serialNumber);

  long deleteBySerialNumber(String serialNumber);
}
