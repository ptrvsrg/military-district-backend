package ru.nsu.ccfit.petrov.database.military_district.formation.util;

import java.util.List;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.ArmyFilter;
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.BrigadeFilter;
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.CompanyFilter;
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.CorpsFilter;
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.DivisionFilter;
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.Pagination;
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.PlatoonFilter;
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.Sorting;
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.SquadFilter;
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.UnitFilter;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.entity.Army;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.entity.Brigade;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.entity.Company;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.entity.Corps;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.entity.Division;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.entity.Platoon;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.entity.Squad;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.entity.Unit;

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

  public static @NonNull Specification<Army> generateArmySpec(ArmyFilter filter) {
    if (filter == null) {
      return (root, query, builder) -> builder.conjunction();
    }
    return (root, query, builder) -> {
      var spec = builder.conjunction();
      if (filter.getName() != null) {
        spec = builder.and(spec, builder.like(root.get("name"), "%" + filter.getName() + "%"));
      }
      if (filter.getCommander() != null) {
        spec =
            builder.and(
                spec,
                builder.like(root.get("commander").get("mbn"), "%" + filter.getCommander() + "%"));
      }
      return spec;
    };
  }

  public static @NonNull Specification<Brigade> generateBrigadeSpec(BrigadeFilter filter) {
    if (filter == null) {
      return (root, query, builder) -> builder.conjunction();
    }
    return (root, query, builder) -> {
      var spec = builder.conjunction();
      if (filter.getName() != null) {
        spec = builder.and(spec, builder.like(root.get("name"), "%" + filter.getName() + "%"));
      }
      if (filter.getCommander() != null) {
        spec =
            builder.and(
                spec,
                builder.like(root.get("commander").get("mbn"), "%" + filter.getCommander() + "%"));
      }
      return spec;
    };
  }

  public static @NonNull Specification<Company> generateCompanySpec(CompanyFilter filter) {
    if (filter == null) {
      return (root, query, builder) -> builder.conjunction();
    }
    return (root, query, builder) -> {
      var spec = builder.conjunction();
      if (filter.getName() != null) {
        spec = builder.and(spec, builder.like(root.get("name"), "%" + filter.getName() + "%"));
      }
      if (filter.getCommander() != null) {
        spec =
            builder.and(
                spec,
                builder.like(root.get("commander").get("mbn"), "%" + filter.getCommander() + "%"));
      }
      if (filter.getUnit() != null) {
        spec =
            builder.and(
                spec, builder.like(root.get("unit").get("name"), "%" + filter.getUnit() + "%"));
      }
      return spec;
    };
  }

  public static @NonNull Specification<Corps> generateCorpsSpec(CorpsFilter filter) {
    if (filter == null) {
      return (root, query, builder) -> builder.conjunction();
    }
    return (root, query, builder) -> {
      var spec = builder.conjunction();
      if (filter.getName() != null) {
        spec = builder.and(spec, builder.like(root.get("name"), "%" + filter.getName() + "%"));
      }
      if (filter.getCommander() != null) {
        spec =
            builder.and(
                spec,
                builder.like(root.get("commander").get("mbn"), "%" + filter.getCommander() + "%"));
      }
      return spec;
    };
  }

  public static @NonNull Specification<Division> generateDivisionSpec(DivisionFilter filter) {
    if (filter == null) {
      return (root, query, builder) -> builder.conjunction();
    }
    return (root, query, builder) -> {
      var spec = builder.conjunction();
      if (filter.getName() != null) {
        spec = builder.and(spec, builder.like(root.get("name"), "%" + filter.getName() + "%"));
      }
      if (filter.getCommander() != null) {
        spec =
            builder.and(
                spec,
                builder.like(root.get("commander").get("mbn"), "%" + filter.getCommander() + "%"));
      }
      return spec;
    };
  }

  public static @NonNull Specification<Platoon> generatePlatoonSpec(PlatoonFilter filter) {
    if (filter == null) {
      return (root, query, builder) -> builder.conjunction();
    }
    return (root, query, builder) -> {
      var spec = builder.conjunction();
      if (filter.getName() != null) {
        spec = builder.and(spec, builder.like(root.get("name"), "%" + filter.getName() + "%"));
      }
      if (filter.getCommander() != null) {
        spec =
            builder.and(
                spec,
                builder.like(root.get("commander").get("mbn"), "%" + filter.getCommander() + "%"));
      }
      if (filter.getCompany() != null) {
        spec =
            builder.and(
                spec,
                builder.like(root.get("company").get("name"), "%" + filter.getCompany() + "%"));
      }
      return spec;
    };
  }

  public static @NonNull Specification<Squad> generateSquadSpec(SquadFilter filter) {
    if (filter == null) {
      return (root, query, builder) -> builder.conjunction();
    }
    return (root, query, builder) -> {
      var spec = builder.conjunction();
      if (filter.getName() != null) {
        spec = builder.and(spec, builder.like(root.get("name"), "%" + filter.getName() + "%"));
      }
      if (filter.getCommander() != null) {
        spec =
            builder.and(
                spec,
                builder.like(root.get("commander").get("mbn"), "%" + filter.getCommander() + "%"));
      }
      if (filter.getPlatoon() != null) {
        spec =
            builder.and(
                spec,
                builder.like(root.get("platoon").get("name"), "%" + filter.getPlatoon() + "%"));
      }
      return spec;
    };
  }

  public static @NonNull Specification<Unit> generateUnitSpec(UnitFilter filter) {
    if (filter == null) {
      return (root, query, builder) -> builder.conjunction();
    }
    return (root, query, builder) -> {
      var spec = builder.conjunction();
      if (filter.getName() != null) {
        spec = builder.and(spec, builder.like(root.get("name"), "%" + filter.getName() + "%"));
      }
      if (filter.getCommander() != null) {
        spec =
            builder.and(
                spec,
                builder.like(root.get("commander").get("mbn"), "%" + filter.getCommander() + "%"));
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
