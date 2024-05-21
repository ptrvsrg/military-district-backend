package ru.nsu.ccfit.petrov.database.military_district.report.persistence.converter;

import static java.lang.String.format;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import ru.nsu.ccfit.petrov.database.military_district.report.persistence.entity.ReportParameter;

@Converter
public class ReportParameterListToStringConverter
    implements AttributeConverter<List<ReportParameter>, String> {
  @Override
  public String convertToDatabaseColumn(List<ReportParameter> attribute) {
    return format(
        "{%s}",
        attribute.stream().map(ReportParameter::toString).reduce("", (a, b) -> a + "," + b));
  }

  @Override
  public List<ReportParameter> convertToEntityAttribute(String dbData) {
    if (dbData == null || dbData.equals("{}")) {
      return List.of();
    }
    var processedDbData = dbData.replaceAll("(\\{)|(\\})|(\")|(\\\\)", "");
    processedDbData = processedDbData.substring(1, processedDbData.length() - 1);
    return Arrays.stream(processedDbData.split("\\),\\("))
        .map(
            s -> {
              var splitted = s.split(",", 2);
              if (splitted.length != 2) {
                return null;
              }
              return new ReportParameter(splitted[0], splitted[1].isEmpty() ? null : splitted[1]);
            })
        .filter(Objects::nonNull)
        .toList();
  }
}
