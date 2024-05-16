package ru.nsu.ccfit.petrov.database.military_district.weapon.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WeaponInput {

  @NotNull(message = "validation.weapon.serial-number.not-null")
  private String serialNumber;

  private String type;
  private String unit;
}
