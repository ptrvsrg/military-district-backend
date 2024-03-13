package ru.nsu.ccfit.petrov.database.military_district.military.dto;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;
import ru.nsu.ccfit.petrov.database.military_district.common.SortInput;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MilitarySort {

  private List<SortInput> sorts;

  public Sort generateSort() {
    var fieldSort =
        Set.of("mbn", "firstName", "lastName", "middleName", "birthDate", "avatarUrl", "rank");

    return sorts.stream()
        .filter(sort -> fieldSort.contains(sort.getField()))
        .map(SortInput::generateSort)
        .filter(Objects::nonNull)
        .reduce(Sort::and)
        .orElse(Sort.unsorted());
  }
}
