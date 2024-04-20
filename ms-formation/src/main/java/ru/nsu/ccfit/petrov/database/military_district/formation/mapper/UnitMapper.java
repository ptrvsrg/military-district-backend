package ru.nsu.ccfit.petrov.database.military_district.formation.mapper;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.UnitInput;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.entity.Unit;

@Mapper(
    uses = {AddressMapper.class, CoordinateMapper.class},
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING)
public interface UnitMapper {

  @Mapping(target = "brigades", ignore = true)
  @Mapping(target = "corps", ignore = true)
  @Mapping(target = "divisions", ignore = true)
  @Mapping(target = "companies", ignore = true)
  @Mapping(source = "commander", target = "commander.mbn")
  Unit toEntity(UnitInput unitInput);

  @InheritConfiguration(name = "toEntity")
  void partialUpdate(UnitInput unitInput, @MappingTarget Unit unit);
}
