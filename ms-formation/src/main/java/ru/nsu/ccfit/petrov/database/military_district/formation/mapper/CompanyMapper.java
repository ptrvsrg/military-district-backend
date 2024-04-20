package ru.nsu.ccfit.petrov.database.military_district.formation.mapper;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.CompanyInput;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.entity.Company;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING)
public interface CompanyMapper {

  @Mapping(target = "unit", ignore = true)
  @Mapping(target = "platoons", ignore = true)
  @Mapping(source = "commander", target = "commander.mbn")
  Company toEntity(CompanyInput companyInput);

  @InheritConfiguration(name = "toEntity")
  void partialUpdate(CompanyInput companyInput, @MappingTarget Company company);
}
