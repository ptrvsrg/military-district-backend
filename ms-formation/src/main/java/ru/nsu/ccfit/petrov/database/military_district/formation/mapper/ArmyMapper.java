package ru.nsu.ccfit.petrov.database.military_district.formation.mapper;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.ArmyDto;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.entity.Army;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING)
public interface ArmyMapper {

  @Mapping(target = "brigades", ignore = true)
  @Mapping(target = "corps", ignore = true)
  @Mapping(target = "divisions", ignore = true)
  @Mapping(source = "commander", target = "commander.mbn")
  Army toEntity(ArmyDto armyDto);

  @InheritConfiguration(name = "toEntity")
  void partialUpdate(ArmyDto armyDto, @MappingTarget Army army);
}
