package ru.nsu.ccfit.petrov.database.military_district.formation.persistence.repository;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.entity.Division;

public interface DivisionRepository extends SpecPageSortRepository<Division, Integer> {

  Optional<Division> findByName(String name);

  Set<Division> findByNameIn(Collection<String> names);

  boolean existsByName(String name);

  long deleteByName(String name);
}
