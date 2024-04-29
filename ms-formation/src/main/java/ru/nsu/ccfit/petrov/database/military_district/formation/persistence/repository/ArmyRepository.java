package ru.nsu.ccfit.petrov.database.military_district.formation.persistence.repository;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.entity.Army;

public interface ArmyRepository extends SpecPageSortRepository<Army, Integer> {

  Optional<Army> findByName(String name);

  Set<Army> findByNameIn(Collection<String> names);

  boolean existsByName(String name);

  long deleteByName(String name);
}
