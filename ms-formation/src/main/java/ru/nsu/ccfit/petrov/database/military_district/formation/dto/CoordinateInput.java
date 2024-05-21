package ru.nsu.ccfit.petrov.database.military_district.formation.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CoordinateInput {

  @NotNull(message = "validation.coordinate.not-null")
  private Double lat;

  @NotNull(message = "validation.coordinate.not-null")
  private Double lng;
}
