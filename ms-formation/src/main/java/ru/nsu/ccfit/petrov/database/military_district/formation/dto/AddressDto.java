package ru.nsu.ccfit.petrov.database.military_district.formation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressDto {

  private Integer postCode;
  private String country;
  private String state;
  private String locality;
  private String street;
  private String houseNumber;
}
