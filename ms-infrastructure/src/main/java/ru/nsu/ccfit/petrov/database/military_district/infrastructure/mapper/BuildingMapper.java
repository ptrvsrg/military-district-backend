package ru.nsu.ccfit.petrov.database.military_district.infrastructure.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ru.nsu.ccfit.petrov.database.military_district.infrastructure.dto.BuildingDto;
import ru.nsu.ccfit.petrov.database.military_district.infrastructure.persistence.entity.Building;
import ru.nsu.ccfit.petrov.database.military_district.infrastructure.persistence.entity.Formation;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING,
    uses = {AddressMapper.class, AttributeMapper.class, CoordinateMapper.class})
public interface BuildingMapper {

  @Mapping(source = "unit", target = "unit.name")
  @Mapping(
      target = "companies",
      expression = "java(companyNamesToCompanies(buildingDto.getCompanies()))")
  @Mapping(
      target = "platoons",
      expression = "java(platoonNamesToPlatoons(buildingDto.getPlatoons()))")
  @Mapping(target = "squads", expression = "java(squadNamesToSquads(buildingDto.getSquads()))")
  Building toEntity(BuildingDto buildingDto);

  @AfterMapping
  default void linkBuildingToAttributes(@MappingTarget Building building) {
    building.getAttributes().forEach(buildingAttribute -> buildingAttribute.setBuilding(building));
  }

  @Mapping(source = "unit", target = "unit.name")
  @Mapping(
      target = "companies",
      expression = "java(companyNamesToCompanies(buildingDto.getCompanies()))")
  @Mapping(
      target = "platoons",
      expression = "java(platoonNamesToPlatoons(buildingDto.getPlatoons()))")
  @Mapping(target = "squads", expression = "java(squadNamesToSquads(buildingDto.getSquads()))")
  @Mapping(target = "attributes", ignore = true)
  void partialUpdate(BuildingDto buildingDto, @MappingTarget Building building);

  default Set<Formation> companyNamesToCompanies(Set<String> companies) {
    return companies.stream()
        .map(name -> Formation.builder().name(name).build())
        .collect(Collectors.toSet());
  }

  default Set<Formation> platoonNamesToPlatoons(Set<String> platoons) {
    return platoons.stream()
        .map(name -> Formation.builder().name(name).build())
        .collect(Collectors.toSet());
  }

  default Set<Formation> squadNamesToSquads(Set<String> squads) {
    return squads.stream()
        .map(name -> Formation.builder().name(name).build())
        .collect(Collectors.toSet());
  }
}
