package ru.nsu.ccfit.petrov.database.military_district.infrastructure.persistence.repository;

import jakarta.persistence.QueryHint;
import java.util.Optional;
import org.springframework.data.jpa.repository.QueryHints;
import ru.nsu.ccfit.petrov.database.military_district.infrastructure.persistence.entity.Building;

public interface BuildingRepository extends SpecPageSortRepository<Building, Integer> {

  @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
  Optional<Building> findByNameAndUnit_Name(String name, String unit);

  @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
  boolean existsByNameAndUnit_Name(String name, String unit);

  @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
  long deleteByNameAndUnit_Name(String name, String unit);
}
