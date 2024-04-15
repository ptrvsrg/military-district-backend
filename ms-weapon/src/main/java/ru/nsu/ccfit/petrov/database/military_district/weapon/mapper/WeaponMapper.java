package ru.nsu.ccfit.petrov.database.military_district.weapon.mapper;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ru.nsu.ccfit.petrov.database.military_district.weapon.dto.WeaponDto;
import ru.nsu.ccfit.petrov.database.military_district.weapon.persistence.entity.Weapon;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING)
public interface WeaponMapper {
  @Mapping(source = "unit", target = "unit.name")
  @Mapping(target = "type", ignore = true)
  Weapon toEntity(WeaponDto weaponDto);

  @InheritConfiguration(name = "toEntity")
  void partialUpdate(WeaponDto weaponDto, @MappingTarget Weapon weapon);
}
