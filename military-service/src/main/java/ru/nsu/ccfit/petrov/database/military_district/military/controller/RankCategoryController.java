package ru.nsu.ccfit.petrov.database.military_district.military.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.entity.RankCategory;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.repository.RankCategoryRepository;

@Controller
@RequiredArgsConstructor
public class RankCategoryController {

  private final RankCategoryRepository rankCategoryRepository;

  @QueryMapping
  public List<RankCategory> rankCategories() {
    return rankCategoryRepository.findAll();
  }
}
