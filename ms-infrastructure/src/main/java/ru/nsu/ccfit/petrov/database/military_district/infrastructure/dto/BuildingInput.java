package ru.nsu.ccfit.petrov.database.military_district.infrastructure.dto;

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
public class BuildingInput {

  @NotNull(message = "validation.building.name.not-null")
  private String name;

  private AddressInput address;
  private CoordinateInput coordinate;
  private String unit;
  private Set<AttributeInput> attributes = new HashSet<>();
  private Set<String> companies = new HashSet<>();
  private Set<String> platoons = new HashSet<>();
  private Set<String> squads = new HashSet<>();
}
