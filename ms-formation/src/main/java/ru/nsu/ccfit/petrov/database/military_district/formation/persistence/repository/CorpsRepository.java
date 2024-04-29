package ru.nsu.ccfit.petrov.database.military_district.formation.persistence.repository;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.entity.Corps;

public interface CorpsRepository extends SpecPageSortRepository<Corps, Integer> {

  Optional<Corps> findByName(String name);

  Set<Corps> findByNameIn(Collection<String> names);

  boolean existsByName(String name);

  long deleteByName(String name);
}
