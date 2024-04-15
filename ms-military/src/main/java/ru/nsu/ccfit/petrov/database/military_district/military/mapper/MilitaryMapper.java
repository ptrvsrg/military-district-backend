package ru.nsu.ccfit.petrov.database.military_district.military.mapper;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ru.nsu.ccfit.petrov.database.military_district.military.dto.MilitaryDto;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.entity.Military;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING)
public interface MilitaryMapper {

  @Mapping(target = "unit.name", source = "unit")
  @Mapping(target = "rank", ignore = true)
  @Mapping(target = "specialties", ignore = true)
  @Mapping(target = "attributes", ignore = true)
  Military toEntity(MilitaryDto militaryDto);

  @InheritConfiguration(name = "toEntity")
  void partialUpdate(MilitaryDto militaryDto, @MappingTarget Military military);
}
