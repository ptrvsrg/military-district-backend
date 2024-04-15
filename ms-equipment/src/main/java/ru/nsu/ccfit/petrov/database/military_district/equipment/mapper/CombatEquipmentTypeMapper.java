package ru.nsu.ccfit.petrov.database.military_district.equipment.mapper;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ru.nsu.ccfit.petrov.database.military_district.equipment.dto.CombatEquipmentTypeDto;
import ru.nsu.ccfit.petrov.database.military_district.equipment.persistence.entity.CombatEquipmentType;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING)
public interface CombatEquipmentTypeMapper {

  @Mapping(target = "category", ignore = true)
  @Mapping(target = "attributes", ignore = true)
  CombatEquipmentType toEntity(CombatEquipmentTypeDto combatEquipmentTypeDto);

  @InheritConfiguration(name = "toEntity")
  void partialUpdate(
      CombatEquipmentTypeDto combatEquipmentTypeDto,
      @MappingTarget CombatEquipmentType combatEquipmentType);
}
