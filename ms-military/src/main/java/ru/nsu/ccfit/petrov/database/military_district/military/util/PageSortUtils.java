package ru.nsu.ccfit.petrov.database.military_district.military.util;

import java.util.List;
import java.util.Objects;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@UtilityClass
public class PageSortUtils {

  public static Pageable generatePageable(Integer page, Integer pageSize, Sort sort) {
    Pageable pageable = null;
    if (ObjectUtils.allNotNull(page, pageSize)) {
      pageable = PageRequest.of(page, pageSize).withSort(sort == null ? Sort.unsorted() : sort);
    }
    return pageable;
  }

  public static Sort generateSort(
      String sortField, Boolean sortAsc, List<String> availableSortFields) {
    Sort sort = Sort.unsorted();

    if (!StringUtils.equalsAny(sortField, availableSortFields.toArray(new String[0]))) {
      return sort;
    }
    if (Objects.nonNull(sortField)) {
      sort =
          (Objects.isNull(sortAsc) || sortAsc)
              ? Sort.by(sortField).ascending()
              : Sort.by(sortField).descending();
    }
    return sort;
  }
}
