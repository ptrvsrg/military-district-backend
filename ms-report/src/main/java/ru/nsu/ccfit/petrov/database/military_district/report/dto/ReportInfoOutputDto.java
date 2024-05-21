package ru.nsu.ccfit.petrov.database.military_district.report.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportInfoOutputDto {

  private String name;
  private String description;
  private List<String> parameters = new ArrayList<>();
}
