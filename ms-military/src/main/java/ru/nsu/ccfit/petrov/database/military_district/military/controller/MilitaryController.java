package ru.nsu.ccfit.petrov.database.military_district.military.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import ru.nsu.ccfit.petrov.database.military_district.military.dto.MilitaryFilter;
import ru.nsu.ccfit.petrov.database.military_district.military.dto.MilitaryInput;
import ru.nsu.ccfit.petrov.database.military_district.military.dto.Pagination;
import ru.nsu.ccfit.petrov.database.military_district.military.dto.Sorting;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.entity.Military;
import ru.nsu.ccfit.petrov.database.military_district.military.service.MilitaryService;

@Controller
@RequiredArgsConstructor
public class MilitaryController {

  private final MilitaryService militaryService;

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_MILITARIES')")
  public List<Military> getMilitaries(
      @Argument("filter") MilitaryFilter filter,
      @Argument("pagination") Pagination pagination,
      @Argument("sorts") List<Sorting> sorts) {
    return militaryService.getAll(filter, pagination, sorts);
  }

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_MILITARIES')")
  public long getMilitaryCount(@Argument("filter") MilitaryFilter filter) {
    return militaryService.getAllCount(filter);
  }

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_MILITARIES')")
  public Military getMilitary(@Argument("mbn") @NonNull String mbn) {
    return militaryService.getByMbn(mbn);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_MILITARIES')")
  public Military createMilitary(@Argument("input") @Valid @NonNull MilitaryInput militaryInput) {
    return militaryService.create(militaryInput);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_MILITARIES')")
  public Military updateMilitary(
      @Argument("mbn") @NonNull String mbn,
      @Argument("input") @Valid @NonNull MilitaryInput militaryInput) {
    return militaryService.update(mbn, militaryInput);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_MILITARIES')")
  public long deleteMilitary(@Argument("mbn") @NonNull String mbn) {
    return militaryService.delete(mbn);
  }
}
