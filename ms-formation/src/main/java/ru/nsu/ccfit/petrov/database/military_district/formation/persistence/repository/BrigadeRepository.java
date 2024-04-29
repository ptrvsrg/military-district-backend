package ru.nsu.ccfit.petrov.database.military_district.formation.persistence.repository;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.entity.Brigade;

public interface BrigadeRepository extends SpecPageSortRepository<Brigade, Integer> {

  Optional<Brigade> findByName(String name);

  Set<Brigade> findByNameIn(Collection<String> names);

  boolean existsByName(String name);

  long deleteByName(String name);
}
