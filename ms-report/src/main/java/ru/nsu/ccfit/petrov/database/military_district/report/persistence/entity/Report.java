package ru.nsu.ccfit.petrov.database.military_district.report.persistence.entity;

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
public class Report {
  private String name;
  private List<String> parameters = new ArrayList<>();
  private List<String> columns = new ArrayList<>();
  private List<Map<String, String>> data = new ArrayList<>();
}
