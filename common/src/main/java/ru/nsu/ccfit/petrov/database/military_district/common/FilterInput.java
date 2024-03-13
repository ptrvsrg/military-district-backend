package ru.nsu.ccfit.petrov.database.military_district.common;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FilterInput {

  private Operator operator;
  private String value;

  public Predicate generateCriteria(CriteriaBuilder builder, Path field) {
    try {
      int v = Integer.parseInt(value);
      switch (operator) {
        case LT:
          return builder.lt(field, v);
        case LE:
          return builder.le(field, v);
        case GT:
          return builder.gt(field, v);
        case GE:
          return builder.ge(field, v);
        case EQ:
          return builder.equal(field, v);
      }
    } catch (NumberFormatException e) {
      switch (operator) {
        case ENDS_WITH:
          return builder.like(field, "%" + value);
        case STARTS_WITH:
          return builder.like(field, value + "%");
        case CONTAINS:
          return builder.like(field, "%" + value + "%");
        case EQ:
          return builder.equal(field, value);
      }
    }

    return null;
  }
}
