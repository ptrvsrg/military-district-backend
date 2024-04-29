package ru.nsu.ccfit.petrov.database.military_district.formation.persistence.repository;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.entity.Unit;

public interface UnitRepository extends SpecPageSortRepository<Unit, Integer> {

  Optional<Unit> findByName(String name);

  Set<Unit> findByNameIn(Collection<String> names);

  boolean existsByName(String name);

  long deleteByName(String name);
}
