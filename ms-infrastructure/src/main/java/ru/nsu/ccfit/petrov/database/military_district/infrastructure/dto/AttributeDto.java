package ru.nsu.ccfit.petrov.database.military_district.infrastructure.dto;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttributeDto implements Serializable {

  @NotNull(message = "validation.attribute.name.not-null")
  private String name;

  @NotNull(message = "validation.attribute.value.not-null")
  private String value;
}
