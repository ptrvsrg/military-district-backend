package ru.nsu.ccfit.petrov.database.military_district.weapon.mapper;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ru.nsu.ccfit.petrov.database.military_district.weapon.dto.WeaponInput;
import ru.nsu.ccfit.petrov.database.military_district.weapon.persistence.entity.Weapon;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING)
public interface WeaponMapper {
  @Mapping(source = "unit", target = "unit.name")
  @Mapping(target = "type", ignore = true)
  Weapon toEntity(WeaponInput weaponInput);

  @InheritConfiguration(name = "toEntity")
  void partialUpdate(WeaponInput weaponInput, @MappingTarget Weapon weapon);
}
