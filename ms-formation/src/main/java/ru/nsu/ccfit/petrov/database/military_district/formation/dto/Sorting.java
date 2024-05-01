package ru.nsu.ccfit.petrov.database.military_district.formation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Sorting {

  private String field;
  private boolean sortAsc = true;
}
