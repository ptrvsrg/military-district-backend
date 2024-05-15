package ru.nsu.ccfit.petrov.database.military_district.report.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportBuildOutputDto {

  private List<Map<String, String>> data = new ArrayList<>();
}
