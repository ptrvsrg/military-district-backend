package ru.nsu.ccfit.petrov.database.military_district.formation.persistence.repository;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.entity.Company;

public interface CompanyRepository extends SpecPageSortRepository<Company, Integer> {

  Optional<Company> findByName(String name);

  Set<Company> findByNameIn(Collection<String> names);

  boolean existsByName(String name);

  long deleteByName(String name);
}
