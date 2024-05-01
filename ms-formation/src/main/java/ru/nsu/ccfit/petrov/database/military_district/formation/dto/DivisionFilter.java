package ru.nsu.ccfit.petrov.database.military_district.formation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DivisionFilter {

  private String name;
  private String commander;
}
