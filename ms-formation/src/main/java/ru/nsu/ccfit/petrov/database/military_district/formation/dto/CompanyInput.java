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
public class CompanyInput {

  @NotNull(message = "validation.company.name.not-null")
  private String name;

  private String commander;
  private String unit;
}
