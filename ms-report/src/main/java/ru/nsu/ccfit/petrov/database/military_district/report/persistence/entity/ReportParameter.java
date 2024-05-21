package ru.nsu.ccfit.petrov.database.military_district.report.persistence.entity;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Struct;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Struct(
    name = "coordinate",
    attributes = {"name", "query_for_values"})
public class ReportParameter implements Serializable {

  public String name;
  public String queryForValues;

  @Override
  public String toString() {
    return String.format("(%s,\"%s\")", name, queryForValues);
  }
}
