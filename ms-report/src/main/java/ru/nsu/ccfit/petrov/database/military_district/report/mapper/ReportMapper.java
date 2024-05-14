package ru.nsu.ccfit.petrov.database.military_district.report.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.nsu.ccfit.petrov.database.military_district.report.dto.ReportInfoOutputDto;
import ru.nsu.ccfit.petrov.database.military_district.report.persistence.entity.Report;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReportMapper {

  ReportInfoOutputDto toOutputDto(Report report);
}
