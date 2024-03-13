package ru.nsu.ccfit.petrov.database.military_district.common;

import static ru.nsu.ccfit.petrov.database.military_district.common.SortDirection.ASC;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SortInput {

  private String field;
  private SortDirection direction;

  public Sort generateSort() {
    if (field == null) {
      return null;
    }
    return direction == ASC || direction == null
        ? Sort.by(field).ascending()
        : Sort.by(field).descending();
  }
}
