package ru.nsu.ccfit.petrov.database.military_district.formation.persistence.repository;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.entity.Squad;

public interface SquadRepository extends SpecPageSortRepository<Squad, Integer> {

  Optional<Squad> findByName(String name);

  Set<Squad> findByNameIn(Collection<String> names);

  boolean existsByName(String name);

  long deleteByName(String name);
}
