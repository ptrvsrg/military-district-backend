package ru.nsu.ccfit.petrov.database.military_district.equipment.persistence.repository;

import jakarta.persistence.QueryHint;
import java.util.Optional;
import org.springframework.data.jpa.repository.QueryHints;
import ru.nsu.ccfit.petrov.database.military_district.equipment.persistence.entity.CombatEquipment;

public interface CombatEquipmentRepository
    extends SpecPageSortRepository<CombatEquipment, Integer> {

  @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
  Optional<CombatEquipment> findBySerialNumber(String serialNumber);

  @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
  boolean existsBySerialNumber(String serialNumber);

  long deleteBySerialNumber(String serialNumber);
}
