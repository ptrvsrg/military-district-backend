package ru.nsu.ccfit.petrov.database.military_district.formation.dto;

import jakarta.validation.constraints.NotNull;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UnitDto {

  @NotNull(message = "validation.unit.name.not-null")
  private String name;

  private CoordinateDto coordinate;
  private AddressDto address;
  private String commander;
  private Set<String> brigades = new LinkedHashSet<>();
  private Set<String> companies = new LinkedHashSet<>();
  private Set<String> corps = new LinkedHashSet<>();
  private Set<String> divisions = new LinkedHashSet<>();
}
