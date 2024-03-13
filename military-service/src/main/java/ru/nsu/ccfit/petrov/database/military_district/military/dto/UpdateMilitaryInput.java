package ru.nsu.ccfit.petrov.database.military_district.military.dto;

import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateMilitaryInput {

  private String firstName;
  private String lastName;
  private String middleName;
  private Date birthDate;
  private String avatarUrl;
  private String rankName;
  private Integer unitNumber;
  private List<MilitaryAttributeInput> attributes;
}
