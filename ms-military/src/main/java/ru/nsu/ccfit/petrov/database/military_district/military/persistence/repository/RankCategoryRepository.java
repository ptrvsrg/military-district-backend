package ru.nsu.ccfit.petrov.database.military_district.military.persistence.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.lang.NonNull;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.entity.RankCategory;

public interface RankCategoryRepository extends SpecPageSortRepository<RankCategory, Integer> {

  @Override
  @NonNull
  List<RankCategory> findAll();

  Optional<RankCategory> findByName(String name);
}
