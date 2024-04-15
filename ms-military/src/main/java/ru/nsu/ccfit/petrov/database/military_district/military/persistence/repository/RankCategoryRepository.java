package ru.nsu.ccfit.petrov.database.military_district.military.persistence.repository;

import jakarta.persistence.QueryHint;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.lang.NonNull;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.entity.RankCategory;

public interface RankCategoryRepository extends SpecPageSortRepository<RankCategory, Integer> {

  @Override
  @NonNull
  @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
  List<RankCategory> findAll();

  Optional<RankCategory> findByName(String name);
}
