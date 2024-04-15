package ru.nsu.ccfit.petrov.database.military_district.weapon.mapper;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ru.nsu.ccfit.petrov.database.military_district.weapon.dto.AttributeDto;
import ru.nsu.ccfit.petrov.database.military_district.weapon.persistence.entity.WeaponAttribute;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING)
public interface AttributeMapper {

  WeaponAttribute toEntity(AttributeDto attributeDto);

  @InheritConfiguration(name = "toEntity")
  void partialUpdate(AttributeDto attributeDto, @MappingTarget WeaponAttribute weaponAttribute);
}
