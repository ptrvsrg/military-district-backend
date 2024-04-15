package ru.nsu.ccfit.petrov.database.military_district.weapon.mapper;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ru.nsu.ccfit.petrov.database.military_district.weapon.dto.WeaponTypeDto;
import ru.nsu.ccfit.petrov.database.military_district.weapon.persistence.entity.WeaponType;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING)
public interface WeaponTypeMapper {

  @Mapping(target = "category", ignore = true)
  @Mapping(target = "attributes", ignore = true)
  WeaponType toEntity(WeaponTypeDto weaponTypeDto);

  @InheritConfiguration(name = "toEntity")
  void partialUpdate(WeaponTypeDto weaponTypeDto, @MappingTarget WeaponType weaponType);
}
