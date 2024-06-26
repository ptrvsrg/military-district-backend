package ru.nsu.ccfit.petrov.database.military_district.infrastructure.mapper;

import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.nsu.ccfit.petrov.database.military_district.infrastructure.dto.AttributeInput;
import ru.nsu.ccfit.petrov.database.military_district.infrastructure.persistence.entity.Attribute;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING)
public interface AttributeMapper {

  Attribute toEntity(AttributeInput attributeInput);

  Set<Attribute> toEntities(Set<AttributeInput> attributeInputs);
}
