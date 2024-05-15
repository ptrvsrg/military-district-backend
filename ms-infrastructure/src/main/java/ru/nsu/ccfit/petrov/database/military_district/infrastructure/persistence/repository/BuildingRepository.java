package ru.nsu.ccfit.petrov.database.military_district.infrastructure.persistence.repository;

import java.util.Optional;
import ru.nsu.ccfit.petrov.database.military_district.infrastructure.persistence.entity.Building;

public interface BuildingRepository extends SpecPageSortRepository<Building, Integer> {

  Optional<Building> findByNameAndUnit_Name(String name, String unit);

  boolean existsByNameAndUnit_Name(String name, String unit);

  long deleteByNameAndUnit_Name(String name, String unit);
}
