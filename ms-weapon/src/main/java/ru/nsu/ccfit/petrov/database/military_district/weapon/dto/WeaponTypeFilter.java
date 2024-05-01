package ru.nsu.ccfit.petrov.database.military_district.weapon.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WeaponTypeFilter {

  private String name;
  private String category;
}
