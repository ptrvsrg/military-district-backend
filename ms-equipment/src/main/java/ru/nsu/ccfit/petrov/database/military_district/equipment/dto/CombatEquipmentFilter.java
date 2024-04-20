package ru.nsu.ccfit.petrov.database.military_district.equipment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CombatEquipmentFilter {

  private String type;
  private String unit;
}
