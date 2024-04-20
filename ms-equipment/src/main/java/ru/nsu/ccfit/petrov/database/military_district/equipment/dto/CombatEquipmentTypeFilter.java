package ru.nsu.ccfit.petrov.database.military_district.equipment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CombatEquipmentTypeFilter {

  private String name;
  private String category;
}
