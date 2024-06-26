package ru.nsu.ccfit.petrov.database.military_district.formation.mapper;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.DivisionInput;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.entity.Division;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING)
public interface DivisionMapper {

  @Mapping(target = "armies", ignore = true)
  @Mapping(target = "units", ignore = true)
  @Mapping(source = "commander", target = "commander.mbn")
  Division toEntity(DivisionInput divisionInput);

  @InheritConfiguration(name = "toEntity")
  void partialUpdate(DivisionInput divisionInput, @MappingTarget Division division);
}
