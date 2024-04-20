package ru.nsu.ccfit.petrov.database.military_district.formation.dto;

import jakarta.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UnitInput {

  @NotNull(message = "validation.unit.name.not-null")
  private String name;

  private CoordinateInput coordinate;
  private AddressInput address;
  private String commander;
  private Set<String> brigades = new HashSet<>();
  private Set<String> companies = new HashSet<>();
  private Set<String> corps = new HashSet<>();
  private Set<String> divisions = new HashSet<>();
}
