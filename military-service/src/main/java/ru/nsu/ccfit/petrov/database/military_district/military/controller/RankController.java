package ru.nsu.ccfit.petrov.database.military_district.military.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.entity.Rank;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.repository.RankRepository;

@Controller
@RequiredArgsConstructor
public class RankController {

  private final RankRepository rankRepository;

  @QueryMapping
  public List<Rank> ranks() {
    return rankRepository.findAll();
  }

  @QueryMapping
  public List<Rank> ranksByCategory(@Argument("category") String category) {
    return rankRepository.findAllByCategoryName(category);
  }
}
