package ru.nsu.ccfit.petrov.database.military_district.military.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.entity.RankCategory;
import ru.nsu.ccfit.petrov.database.military_district.military.service.RankCategoryService;

@Controller
@RequiredArgsConstructor
public class RankCategoryController {

  private final RankCategoryService rankCategoryService;

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_MILITARIES')")
  public List<RankCategory> getRankCategories() {
    return rankCategoryService.getAll();
  }
}
