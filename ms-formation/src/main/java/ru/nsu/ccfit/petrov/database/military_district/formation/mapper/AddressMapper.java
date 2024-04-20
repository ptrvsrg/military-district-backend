package ru.nsu.ccfit.petrov.database.military_district.formation.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.AddressInput;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.entity.Address;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING)
public interface AddressMapper {

  Address toEntity(AddressInput addressInput);

  void partialUpdate(AddressInput addressInput, @MappingTarget Address address);
}
