package ru.nsu.ccfit.petrov.database.military_district.formation.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.CoordinateDto;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.entity.Coordinate;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING)
public interface CoordinateMapper {

  Coordinate toEntity(CoordinateDto coordinateDto);

  void partialUpdate(CoordinateDto coordinateDto, @MappingTarget Coordinate coordinate);
}
