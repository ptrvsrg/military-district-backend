package ru.nsu.ccfit.petrov.database.military_district.military.dto;

import static ru.nsu.ccfit.petrov.database.military_district.military.util.ValidationUtils.FIRST_NAME_REGEX;
import static ru.nsu.ccfit.petrov.database.military_district.military.util.ValidationUtils.LAST_NAME_REGEX;
import static ru.nsu.ccfit.petrov.database.military_district.military.util.ValidationUtils.MIDDLE_NAME_REGEX;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MilitaryInput {

  @NotNull(message = "{validation.military.mbn.not-null}")
  private String mbn;

  @NotNull(message = "{validation.military.first-name.not-null}")
  @Pattern(regexp = FIRST_NAME_REGEX, message = "{validation.military.first-name.pattern}")
  private String firstName;

  @NotNull(message = "{validation.military.last-name.not-null}")
  @Pattern(regexp = LAST_NAME_REGEX, message = "{validation.military.last-name.pattern}")
  private String lastName;

  @Pattern(regexp = MIDDLE_NAME_REGEX, message = "{validation.military.middle-name.pattern}")
  private String middleName;

  @NotNull(message = "{validation.military.birth-date.not-null}")
  @Past(message = "{validation.military.birth-date.past}")
  private LocalDate birthDate;

  private String rank;

  private Set<AttributeInput> attributes = new HashSet<>();

  private Set<String> specialties = new HashSet<>();

  private String unit;
}
