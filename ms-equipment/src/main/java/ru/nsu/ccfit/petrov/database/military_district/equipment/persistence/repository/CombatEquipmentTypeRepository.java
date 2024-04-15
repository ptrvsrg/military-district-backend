package ru.nsu.ccfit.petrov.database.military_district.equipment.persistence.repository;

import jakarta.persistence.QueryHint;
import java.util.Optional;
import org.springframework.data.jpa.repository.QueryHints;
import ru.nsu.ccfit.petrov.database.military_district.equipment.persistence.entity.CombatEquipmentType;

public interface CombatEquipmentTypeRepository
    extends SpecPageSortRepository<CombatEquipmentType, Integer> {

  @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
  Optional<CombatEquipmentType> findByName(String name);

  @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
  Optional<CombatEquipmentType> findByNameAndCategory_Name(String name, String name1);

  @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
  boolean existsByNameAndCategory_Name(String name, String name1);

  long deleteByNameAndCategory_Name(String name, String category);
}
