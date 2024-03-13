package ru.nsu.ccfit.petrov.database.military_district.military.dto;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MilitaryAttributeInput {

  @NotNull(message = "{validation.attribute.name.not-null}")
  private String name;

  @NotNull(message = "{validation.attribute.rank-name.not-null}")
  private String rankName;

  @NotNull(message = "{validation.attribute.value.not-null}")
  private String value;
}
