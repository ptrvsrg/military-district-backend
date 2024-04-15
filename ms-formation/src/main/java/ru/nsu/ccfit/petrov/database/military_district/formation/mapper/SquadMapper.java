package ru.nsu.ccfit.petrov.database.military_district.formation.mapper;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.SquadDto;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.entity.Squad;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING)
public interface SquadMapper {

  @Mapping(source = "platoon", target = "platoon.name")
  @Mapping(source = "commander", target = "commander.mbn")
  Squad toEntity(SquadDto squadDto);

  @InheritConfiguration(name = "toEntity")
  void partialUpdate(SquadDto squadDto, @MappingTarget Squad squad);
}
