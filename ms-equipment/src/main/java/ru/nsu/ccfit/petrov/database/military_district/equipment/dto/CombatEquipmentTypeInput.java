package ru.nsu.ccfit.petrov.database.military_district.equipment.dto;

import jakarta.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CombatEquipmentTypeInput {

  @NotNull(message = "validation.combat-equipment-type.name.not-null")
  private String name;

  @NotNull(message = "validation.combat-equipment-type.category.not-null")
  private String category;

  private Set<AttributeInput> attributes = new HashSet<>();
}
