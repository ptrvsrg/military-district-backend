package ru.nsu.ccfit.petrov.database.military_district.formation.persistence.repository;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.entity.Platoon;

public interface PlatoonRepository extends SpecPageSortRepository<Platoon, Integer> {

  Optional<Platoon> findByName(String name);

  Set<Platoon> findByNameIn(Collection<String> names);

  boolean existsByName(String name);

  long deleteByName(String name);
}
