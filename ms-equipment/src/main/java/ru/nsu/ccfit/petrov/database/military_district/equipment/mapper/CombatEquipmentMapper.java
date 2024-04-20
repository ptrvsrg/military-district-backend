package ru.nsu.ccfit.petrov.database.military_district.equipment.mapper;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ru.nsu.ccfit.petrov.database.military_district.equipment.dto.CombatEquipmentInput;
import ru.nsu.ccfit.petrov.database.military_district.equipment.persistence.entity.CombatEquipment;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING)
public interface CombatEquipmentMapper {
  @Mapping(source = "unit", target = "unit.name")
  @Mapping(target = "type", ignore = true)
  CombatEquipment toEntity(CombatEquipmentInput combatEquipmentInput);

  @InheritConfiguration(name = "toEntity")
  void partialUpdate(
      CombatEquipmentInput combatEquipmentInput, @MappingTarget CombatEquipment combatEquipment);
}
