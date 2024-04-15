package ru.nsu.ccfit.petrov.database.military_district.formation.mapper;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.PlatoonDto;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.entity.Platoon;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING)
public interface PlatoonMapper {

  @Mapping(target = "squads", ignore = true)
  @Mapping(source = "company", target = "company.name")
  @Mapping(source = "commander", target = "commander.mbn")
  Platoon toEntity(PlatoonDto platoonDto);

  @InheritConfiguration(name = "toEntity")
  void partialUpdate(PlatoonDto platoonDto, @MappingTarget Platoon platoon);
}
