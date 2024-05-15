package ru.nsu.ccfit.petrov.database.military_district.infrastructure.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ru.nsu.ccfit.petrov.database.military_district.infrastructure.dto.CoordinateInput;
import ru.nsu.ccfit.petrov.database.military_district.infrastructure.persistence.entity.Coordinate;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING)
public interface CoordinateMapper {

  Coordinate toEntity(CoordinateInput coordinateInput);

  void partialUpdate(CoordinateInput coordinateInput, @MappingTarget Coordinate coordinate);
}
