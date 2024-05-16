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
public class ArmyInput {

  @NotNull(message = "validation.army.name.not-null")
  private String name;

  private String commander;
  private Set<String> brigades = new HashSet<>();
  private Set<String> corps = new HashSet<>();
  private Set<String> divisions = new HashSet<>();
}
