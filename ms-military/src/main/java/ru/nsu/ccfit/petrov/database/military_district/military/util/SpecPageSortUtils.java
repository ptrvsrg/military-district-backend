package ru.nsu.ccfit.petrov.database.military_district.military.util;

import java.util.List;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import ru.nsu.ccfit.petrov.database.military_district.military.dto.AttributeDefinitionFilter;
import ru.nsu.ccfit.petrov.database.military_district.military.dto.MilitaryFilter;
import ru.nsu.ccfit.petrov.database.military_district.military.dto.Pagination;
import ru.nsu.ccfit.petrov.database.military_district.military.dto.RankFilter;
import ru.nsu.ccfit.petrov.database.military_district.military.dto.Sorting;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.entity.Attribute;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.entity.Military;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.entity.Rank;

@UtilityClass
public class SpecPageSortUtils {

  public static @Nullable Pageable generatePageable(
      @Nullable Pagination pagination, @NonNull Sort sort) {
    if (pagination == null) {
      return null;
    }
    return PageRequest.of(pagination.getPage(), pagination.getPageSize()).withSort(sort);
  }

  public static @NonNull Sort generateSort(
      List<Sorting> sorts, @NonNull List<String> availableSortFields) {
    if (sorts == null) {
      sorts = List.of();
    }
    return sorts.stream()
        .filter(
            s -> StringUtils.equalsAny(s.getField(), availableSortFields.toArray(new String[0])))
        .map(s -> Sort.by(s.isSortAsc() ? Sort.Direction.ASC : Sort.Direction.DESC, s.getField()))
        .reduce(Sort.unsorted(), Sort::and);
  }

  public static Specification<Attribute> generateAttributeDefinitionSpec(
      AttributeDefinitionFilter filter) {
    if (filter == null) {
      return (root, query, builder) -> builder.conjunction();
    }
    return (root, query, builder) -> {
      var spec = builder.conjunction();
      if (filter.getRank() != null) {
        spec =
            builder.and(
                spec, builder.like(root.get("rank").get("name"), "%" + filter.getRank() + "%"));
      }
      return spec;
    };
  }

  public static Specification<Military> generateMilitarySpec(MilitaryFilter filter) {
    if (filter == null) {
      return (root, query, builder) -> builder.conjunction();
    }
    return (root, query, builder) -> {
      var spec = builder.conjunction();
      if (filter.getName() != null) {
        var concatParts =
                List.of(
                        builder.concat(root.get("firstName"), " "),
                        builder.concat(root.get("lastName"), " "),
                        builder.concat(root.get("middleName"), ""));
        var nameExpr = concatParts.stream().reduce(builder::concat).get();
        spec = builder.and(spec, builder.like(nameExpr, "%" + filter.getName() + "%"));
      }
      if (filter.getRank() != null) {
        spec =
            builder.and(
                spec, builder.like(root.get("rank").get("name"), "%" + filter.getRank() + "%"));
      }
      if (filter.getUnit() != null) {
        spec =
            builder.and(
                spec, builder.like(root.get("unit").get("name"), "%" + filter.getUnit() + "%"));
      }
      return spec;
    };
  }

  public static Specification<Rank> generateRankSpec(RankFilter filter) {
    if (filter == null) {
      return (root, query, builder) -> builder.conjunction();
    }
    return (root, query, builder) -> {
      var spec = builder.conjunction();
      if (filter.getName() != null) {
        spec = builder.and(spec, builder.like(root.get("name"), "%" + filter.getName() + "%"));
      }
      return spec;
    };
  }
}
