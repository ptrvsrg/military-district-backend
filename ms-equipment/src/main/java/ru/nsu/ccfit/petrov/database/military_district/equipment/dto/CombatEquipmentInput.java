package ru.nsu.ccfit.petrov.database.military_district.equipment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CombatEquipmentInput {

  @NotNull(message = "validation.combat-equipment.serial-number.not-null")
  private String serialNumber;

  private String type;
  private String unit;
}
