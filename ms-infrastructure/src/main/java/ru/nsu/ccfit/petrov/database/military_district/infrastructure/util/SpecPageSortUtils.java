package ru.nsu.ccfit.petrov.database.military_district.infrastructure.util;

import java.util.List;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import ru.nsu.ccfit.petrov.database.military_district.infrastructure.dto.BuildingFilter;
import ru.nsu.ccfit.petrov.database.military_district.infrastructure.dto.Pagination;
import ru.nsu.ccfit.petrov.database.military_district.infrastructure.dto.Sorting;
import ru.nsu.ccfit.petrov.database.military_district.infrastructure.persistence.entity.Building;

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

  public static @NonNull Specification<Building> generateBuildingSpec(BuildingFilter filter) {
    if (filter == null) {
      return (root, query, builder) -> builder.conjunction();
    }
    return (root, query, builder) -> {
      var spec = builder.conjunction();
      if (filter.getName() != null) {
        spec = builder.and(spec, builder.like(root.get("name"), "%" + filter.getName() + "%"));
      }
      if (filter.getUnit() != null) {
        spec =
            builder.and(
                spec, builder.like(root.get("unit").get("name"), "%" + filter.getUnit() + "%"));
      }
      if (filter.getAddress() != null) {
        var concatParts =
            List.of(
                builder.concat(root.get("address").get("country"), ", "),
                builder.concat(root.get("address").get("state"), ", "),
                builder.concat(root.get("address").get("locality"), ", "),
                builder.concat(root.get("address").get("street"), ", "),
                builder.concat(root.get("address").get("houseNumber"), ", "),
                builder.concat(root.get("address").get("postCode").as(String.class), ", "));
        var addressExpr = concatParts.stream().reduce(builder::concat).get();
        spec = builder.and(spec, builder.like(addressExpr, "%" + filter.getAddress() + "%"));
      }
      return spec;
    };
  }
}
