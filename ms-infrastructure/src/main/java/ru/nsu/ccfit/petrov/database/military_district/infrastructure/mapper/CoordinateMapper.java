package ru.nsu.ccfit.petrov.database.military_district.infrastructure.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ru.nsu.ccfit.petrov.database.military_district.infrastructure.dto.CoordinateDto;
import ru.nsu.ccfit.petrov.database.military_district.infrastructure.persistence.entity.Coordinate;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING)
public interface CoordinateMapper {

  Coordinate toEntity(CoordinateDto coordinateDto);

  void partialUpdate(CoordinateDto coordinateDto, @MappingTarget Coordinate coordinate);
}
