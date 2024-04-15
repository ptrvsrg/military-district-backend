package ru.nsu.ccfit.petrov.database.military_district.formation.persistence.repository;

import jakarta.persistence.QueryHint;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.QueryHints;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.entity.Unit;

public interface UnitRepository extends SpecPageSortRepository<Unit, Integer> {

  @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
  Optional<Unit> findByName(String name);

  @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
  Set<Unit> findByNameIn(Collection<String> names);

  @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
  boolean existsByName(String name);

  long deleteByName(String name);
}
