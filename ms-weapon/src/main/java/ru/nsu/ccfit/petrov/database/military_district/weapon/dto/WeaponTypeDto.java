package ru.nsu.ccfit.petrov.database.military_district.weapon.dto;

import jakarta.validation.constraints.NotNull;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WeaponTypeDto {

  @NotNull(message = "validation.weapon-type.name.not-null")
  private String name;

  @NotNull(message = "validation.weapon-type.category.not-null")
  private String category;

  private Set<AttributeDto> attributes = new LinkedHashSet<>();
}
