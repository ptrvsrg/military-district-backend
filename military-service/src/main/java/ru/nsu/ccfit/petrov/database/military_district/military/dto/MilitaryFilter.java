package ru.nsu.ccfit.petrov.database.military_district.military.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import ru.nsu.ccfit.petrov.database.military_district.common.FilterInput;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.entity.Military;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MilitaryFilter {

  private List<FilterInput> name;
  private List<FilterInput> rank;

  public Specification<Military> generateSpecification() {
    Specification<Military> spec = null;
    if (name != null) {
      spec =
          name.stream().map(this::byName).reduce(null, (f1, f2) -> (f1 == null) ? f2 : f1.or(f2));
    }
    if (rank != null) {
      var andSpec =
          rank.stream().map(this::byRank).reduce(null, (f1, f2) -> (f1 == null) ? f2 : f1.or(f2));
      spec = (spec == null) ? andSpec : spec.and(andSpec);
    }

    return spec;
  }

  private Specification<Military> byName(FilterInput filterInput) {
    return (root, query, builder) ->
        builder.or(
            filterInput.generateCriteria(builder, root.get("firstName")),
            filterInput.generateCriteria(builder, root.get("lastName")));
  }

  private Specification<Military> byRank(FilterInput filterInput) {
    return (root, query, builder) ->
        filterInput.generateCriteria(builder, root.get("rank").get("name"));
  }
}
