package ru.nsu.ccfit.petrov.database.military_district.military.persistence.repository;

import java.util.Optional;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.entity.Military;

public interface MilitaryRepository extends SpecPageSortRepository<Military, Integer> {

  Optional<Military> findByMbn(String mbn);

  boolean existsByMbn(String mbn);

  long deleteByMbn(String mbn);
}
