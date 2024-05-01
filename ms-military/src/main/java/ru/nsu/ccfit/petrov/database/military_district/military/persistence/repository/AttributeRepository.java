package ru.nsu.ccfit.petrov.database.military_district.military.persistence.repository;

import java.util.Optional;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.entity.Attribute;

public interface AttributeRepository extends SpecPageSortRepository<Attribute, Integer> {
  Optional<Attribute> findByNameAndRank_Name(String name, String name1);
}
