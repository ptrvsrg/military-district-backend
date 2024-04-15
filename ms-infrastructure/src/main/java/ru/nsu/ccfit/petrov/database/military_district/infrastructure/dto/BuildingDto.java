package ru.nsu.ccfit.petrov.database.military_district.infrastructure.dto;

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
public class BuildingDto {

  @NotNull(message = "validation.building.name.not-null")
  private String name;

  private AddressDto address;
  private CoordinateDto coordinate;
  private String unit;
  private Set<AttributeDto> attributes = new LinkedHashSet<>();
  private Set<String> companies = new LinkedHashSet<>();
  private Set<String> platoons = new LinkedHashSet<>();
  private Set<String> squads = new LinkedHashSet<>();
}
