package ru.nsu.ccfit.petrov.database.military_district.weapon.util;

import java.util.List;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import ru.nsu.ccfit.petrov.database.military_district.weapon.dto.Pagination;
import ru.nsu.ccfit.petrov.database.military_district.weapon.dto.Sorting;
import ru.nsu.ccfit.petrov.database.military_district.weapon.dto.WeaponFilter;
import ru.nsu.ccfit.petrov.database.military_district.weapon.dto.WeaponTypeFilter;
import ru.nsu.ccfit.petrov.database.military_district.weapon.persistence.entity.Weapon;
import ru.nsu.ccfit.petrov.database.military_district.weapon.persistence.entity.WeaponType;

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

  public static Specification<Weapon> generateWeaponSpec(WeaponFilter filter) {
    if (filter == null) {
      return (root, query, builder) -> builder.conjunction();
    }
    return (root, query, builder) -> {
      var spec = builder.conjunction();
      if (filter.getType() != null) {
        spec =
            builder.and(
                spec, builder.like(root.get("type").get("name"), "%" + filter.getType() + "%"));
      }
      if (filter.getUnit() != null) {
        spec =
            builder.and(
                spec, builder.like(root.get("unit").get("name"), "%" + filter.getUnit() + "%"));
      }
      return spec;
    };
  }

  public static Specification<WeaponType> generateWeaponTypeSpec(WeaponTypeFilter filter) {
    if (filter == null) {
      return (root, query, builder) -> builder.conjunction();
    }
    return (root, query, builder) -> {
      var spec = builder.conjunction();
      if (filter.getName() != null) {
        spec = builder.and(spec, builder.like(root.get("name"), "%" + filter.getName() + "%"));
      }
      if (filter.getCategory() != null) {
        spec =
            builder.and(
                spec,
                builder.like(root.get("category").get("name"), "%" + filter.getCategory() + "%"));
      }
      return spec;
    };
  }
}
