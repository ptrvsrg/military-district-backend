package ru.nsu.ccfit.petrov.database.military_district.military.persistence.repository;

import java.util.Optional;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.entity.Rank;

public interface RankRepository extends SpecPageSortRepository<Rank, Integer> {

  Optional<Rank> findByName(String name);
}
