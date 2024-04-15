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
public class BrigadeDto {

  @NotNull(message = "validation.brigade.name.not-null")
  private String name;

  private String commander;
  private Set<String> armies = new LinkedHashSet<>();
  private Set<String> units = new LinkedHashSet<>();
}
