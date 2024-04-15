package ru.nsu.ccfit.petrov.database.military_district.military.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.entity.Rank;
import ru.nsu.ccfit.petrov.database.military_district.military.service.RankService;

@Controller
@RequiredArgsConstructor
public class RankController {

  private final RankService rankService;

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_MILITARIES)")
  public List<Rank> getRanks(@Argument("category") String category) {
    return rankService.getAll(category);
  }
}
