package ru.nsu.ccfit.petrov.database.military_district.military.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Pagination {

  private int page;
  private int pageSize;
}
