package ru.nsu.ccfit.petrov.database.military_district.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaginationInput {

  private int page;
  private int pageSize;

  public PageRequest generatePageable() {
    return PageRequest.of(page, pageSize);
  }
}
