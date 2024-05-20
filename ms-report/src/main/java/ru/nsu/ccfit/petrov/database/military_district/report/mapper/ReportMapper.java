package ru.nsu.ccfit.petrov.database.military_district.report.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.nsu.ccfit.petrov.database.military_district.report.dto.ReportInfoOutputDto;
import ru.nsu.ccfit.petrov.database.military_district.report.persistence.entity.Report;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReportMapper {

  @Mapping(
      target = "parameters",
      expression =
          "java(report.getParameters().stream().map(ru.nsu.ccfit.petrov.database.military_district.report.persistence.entity.ReportParameter::getName).toList())")
  ReportInfoOutputDto toOutputDto(Report report);
}
