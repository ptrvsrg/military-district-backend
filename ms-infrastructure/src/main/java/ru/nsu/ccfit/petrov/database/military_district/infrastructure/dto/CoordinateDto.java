package ru.nsu.ccfit.petrov.database.military_district.infrastructure.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CoordinateDto {

  @NotNull(message = "validation.coordinate.not-null")
  private Double lat;

  @NotNull(message = "validation.coordinate.not-null")
  private Double lng;
}
