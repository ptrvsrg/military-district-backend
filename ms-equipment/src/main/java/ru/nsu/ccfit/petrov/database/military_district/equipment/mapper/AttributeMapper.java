package ru.nsu.ccfit.petrov.database.military_district.equipment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.nsu.ccfit.petrov.database.military_district.equipment.dto.AttributeInput;
import ru.nsu.ccfit.petrov.database.military_district.equipment.persistence.entity.CombatEquipmentAttribute;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING)
public interface AttributeMapper {

  CombatEquipmentAttribute toEntity(AttributeInput attributeInput);
}
