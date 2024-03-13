package ru.nsu.ccfit.petrov.database.military_district.military.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import ru.nsu.ccfit.petrov.database.military_district.military.dto.MilitaryAttributeInput;
import ru.nsu.ccfit.petrov.database.military_district.military.exception.MilitaryAttributeDefinitionNotFoundException;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.entity.Military;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.entity.MilitaryAttribute;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.entity.MilitaryAttributeDefinition;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.repository.MilitaryAttributeDefinitionRepository;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class MilitaryAttributeMapper {

  @Autowired private MilitaryAttributeDefinitionRepository militaryAttributeDefinitionRepository;

  @Mapping(
      source = "attributeInput",
      target = "definition",
      qualifiedByName = "findMilitaryAttributeDefinitionByNameAndRankName")
  @Mapping(source = "military", target = "military")
  @Mapping(target = "id", ignore = true)
  public abstract MilitaryAttribute toEntity(
      MilitaryAttributeInput attributeInput, Military military);

  @Named("findMilitaryAttributeDefinitionByNameAndRankName")
  protected MilitaryAttributeDefinition findMilitaryAttributeDefinitionByNameAndRankName(
      MilitaryAttributeInput attributeInput) {
    return militaryAttributeDefinitionRepository
        .findByNameAndRankName(attributeInput.getName(), attributeInput.getRankName())
        .orElseThrow(MilitaryAttributeDefinitionNotFoundException::new);
  }

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  @Mapping(
          source = "attributeInput",
          target = "definition",
          qualifiedByName = "findMilitaryAttributeDefinitionByNameAndRankName")
  public abstract MilitaryAttribute partialUpdate(
      MilitaryAttributeInput attributeInput, @MappingTarget MilitaryAttribute militaryAttribute);
}
