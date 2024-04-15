package ru.nsu.ccfit.petrov.database.military_district.formation.persistence.repository;

import jakarta.persistence.QueryHint;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.QueryHints;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.entity.Army;

public interface ArmyRepository extends SpecPageSortRepository<Army, Integer> {

  @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
  Optional<Army> findByName(String name);

  @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
  Set<Army> findByNameIn(Collection<String> names);

  @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
  boolean existsByName(String name);

  long deleteByName(String name);
}
