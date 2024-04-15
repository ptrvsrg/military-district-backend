package ru.nsu.ccfit.petrov.database.military_district.military.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.nsu.ccfit.petrov.database.military_district.military.dto.AttributeDto;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.entity.Attribute;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING)
public interface AttributeMapper {

  @Mapping(target = "rank", ignore = true)
  Attribute toEntity(AttributeDto attributeDto);
}
