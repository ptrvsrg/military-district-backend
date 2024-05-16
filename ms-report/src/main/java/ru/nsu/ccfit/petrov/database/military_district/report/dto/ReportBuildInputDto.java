package ru.nsu.ccfit.petrov.database.military_district.report.dto;

import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportBuildInputDto {

  private Map<String, String> parameters = new HashMap<>();
}
