package ru.nsu.ccfit.petrov.database.military_district.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BuildingFilter {

  private String name;
  private String address;
  private String unit;
}
