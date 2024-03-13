package ru.nsu.ccfit.petrov.database.military_district.military.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.entity.MilitaryAttributeDefinition;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.repository.MilitaryAttributeDefinitionRepository;

@Controller
@RequiredArgsConstructor
public class MilitaryAttributeDefinitionController {

  private final MilitaryAttributeDefinitionRepository militaryAttributeDefinitionRepository;

  @QueryMapping
  public List<MilitaryAttributeDefinition> militaryAttributeDefinitions() {
    return militaryAttributeDefinitionRepository.findAll();
  }

  @QueryMapping
  public List<MilitaryAttributeDefinition> militaryAttributeDefinitionsByRank(
      @Argument("rank") String rank) {
    return militaryAttributeDefinitionRepository.findAllByRankName(rank);
  }
}
