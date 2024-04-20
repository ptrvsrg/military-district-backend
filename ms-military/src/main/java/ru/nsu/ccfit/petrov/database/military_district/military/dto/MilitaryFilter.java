package ru.nsu.ccfit.petrov.database.military_district.military.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MilitaryFilter {

  private String firstName;
  private String lastName;
  private String middleName;
  private String rank;
  private String unit;
}
