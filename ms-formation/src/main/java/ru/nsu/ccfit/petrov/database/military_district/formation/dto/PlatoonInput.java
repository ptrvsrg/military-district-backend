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
public class PlatoonInput {

  @NotNull(message = "validation.platoon.name.not-null")
  private String name;

  private String commander;
  private String company;
  private Set<String> squads = new HashSet<>();
}
