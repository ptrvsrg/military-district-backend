package ru.nsu.ccfit.petrov.database.military_district.military.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.entity.Attribute;
import ru.nsu.ccfit.petrov.database.military_district.military.service.AttributeDefinitionService;

@Controller
@RequiredArgsConstructor
public class AttributeDefinitionController {

  private final AttributeDefinitionService attributeDefinitionService;

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_MILITARIES')")
  public List<Attribute> getMilitaryAttributeDefinitions(@Argument("rank") String rank) {
    return attributeDefinitionService.getAll(rank);
  }
}
