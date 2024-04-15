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
import ru.nsu.ccfit.petrov.database.military_district.military.dto.MilitaryDto;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.entity.Military;
import ru.nsu.ccfit.petrov.database.military_district.military.service.MilitaryService;

@Controller
@RequiredArgsConstructor
public class MilitaryController {

  private final MilitaryService militaryService;

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_MILITARIES')")
  public List<Military> getMilitaries(
      @Argument("firstName") String firstName,
      @Argument("lastName") String lastName,
      @Argument("middleName") String middleName,
      @Argument("rank") String rank,
      @Argument("unit") String unit,
      @Argument("page") Integer page,
      @Argument("pageSize") Integer pageSize,
      @Argument("sort") String sortField,
      @Argument("sortAsc") Boolean sortAsc) {
    return militaryService.getAll(
        firstName, lastName, middleName, rank, unit, page, pageSize, sortField, sortAsc);
  }

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_MILITARIES')")
  public long getMilitaryCount(
      @Argument("firstName") String firstName,
      @Argument("lastName") String lastName,
      @Argument("middleName") String middleName,
      @Argument("rank") String rank,
      @Argument("unit") String unit) {
    return militaryService.getAllCount(firstName, lastName, middleName, rank, unit);
  }

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_MILITARIES')")
  public Military getMilitary(@Argument("mbn") @NonNull String mbn) {
    return militaryService.getByMbn(mbn);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_MILITARIES')")
  public Military createMilitary(@Argument("input") @Valid @NonNull MilitaryDto militaryDto) {
    return militaryService.create(militaryDto);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_MILITARIES')")
  public Military updateMilitary(
      @Argument("mbn") @NonNull String mbn,
      @Argument("input") @Valid @NonNull MilitaryDto militaryDto) {
    return militaryService.update(mbn, militaryDto);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_MILITARIES')")
  public long deleteMilitary(@Argument("mbn") @NonNull String mbn) {
    return militaryService.delete(mbn);
  }
}
