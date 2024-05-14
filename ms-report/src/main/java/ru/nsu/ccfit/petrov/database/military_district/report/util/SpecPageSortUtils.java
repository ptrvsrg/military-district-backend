package ru.nsu.ccfit.petrov.database.military_district.report.util;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@UtilityClass
public class SpecPageSortUtils {

  public static Pageable generatePageable(Integer page, Integer pageSize, @NonNull Sort sort) {
    if (ObjectUtils.anyNull(page, pageSize)) {
      return Pageable.unpaged(sort);
    }
    return PageRequest.of(page, pageSize, sort);
  }
}
