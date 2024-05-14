package ru.nsu.ccfit.petrov.database.military_district.report.persistence.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.List;

import static java.lang.String.format;

@Converter(autoApply = true)
public class ListToStringConverter implements AttributeConverter<List<String>, String> {
  @Override
  public String convertToDatabaseColumn(List<String> attribute) {
    return format("{%s}", attribute.stream().reduce("", (a, b) -> a + "," + b));
  }

  @Override
  public List<String> convertToEntityAttribute(String dbData) {
    if (dbData == null || dbData.equals("{}")) {
      return List.of();
    }
    return List.of(dbData.replace("{", "").replace("}", "").split(","));
  }
}
