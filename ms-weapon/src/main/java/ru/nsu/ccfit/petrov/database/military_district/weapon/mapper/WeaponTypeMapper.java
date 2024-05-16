package ru.nsu.ccfit.petrov.database.military_district.weapon.mapper;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ru.nsu.ccfit.petrov.database.military_district.weapon.dto.WeaponTypeInput;
import ru.nsu.ccfit.petrov.database.military_district.weapon.persistence.entity.WeaponType;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING)
public interface WeaponTypeMapper {

  @Mapping(target = "category", ignore = true)
  @Mapping(target = "attributes", ignore = true)
  WeaponType toEntity(WeaponTypeInput weaponTypeInput);

  @InheritConfiguration(name = "toEntity")
  void partialUpdate(WeaponTypeInput weaponTypeInput, @MappingTarget WeaponType weaponType);
}
