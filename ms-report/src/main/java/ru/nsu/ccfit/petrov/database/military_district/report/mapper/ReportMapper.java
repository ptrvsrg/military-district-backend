package ru.nsu.ccfit.petrov.database.military_district.report.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.nsu.ccfit.petrov.database.military_district.report.dto.ReportBuildOutputDto;
import ru.nsu.ccfit.petrov.database.military_district.report.dto.ReportOutputDto;
import ru.nsu.ccfit.petrov.database.military_district.report.persistence.entity.Report;
import ru.nsu.ccfit.petrov.database.military_district.report.persistence.entity.ReportProjection;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReportMapper {

  ReportBuildOutputDto toBuildOutputDto(Report report);

  ReportOutputDto toOutputDto(ReportProjection projection);
}
