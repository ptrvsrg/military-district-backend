package ru.nsu.ccfit.petrov.database.military_district.infrastructure.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ru.nsu.ccfit.petrov.database.military_district.infrastructure.dto.AddressDto;
import ru.nsu.ccfit.petrov.database.military_district.infrastructure.persistence.entity.Address;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING)
public interface AddressMapper {

  Address toEntity(AddressDto addressDto);

  void partialUpdate(AddressDto addressDto, @MappingTarget Address address);
}
