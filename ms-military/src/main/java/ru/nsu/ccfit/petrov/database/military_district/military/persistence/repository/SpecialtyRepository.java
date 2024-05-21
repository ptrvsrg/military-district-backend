package ru.nsu.ccfit.petrov.database.military_district.military.persistence.repository;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.entity.Specialty;

public interface SpecialtyRepository extends SpecPageSortRepository<Specialty, Integer> {

  Set<Specialty> findAllByCodeIn(Collection<String> codes);

  Optional<Specialty> findByCode(String code);
}
