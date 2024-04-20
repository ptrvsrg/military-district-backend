package ru.nsu.ccfit.petrov.database.military_district.formation.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.Pagination;
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.PlatoonFilter;
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.PlatoonInput;
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.Sorting;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.entity.Platoon;
import ru.nsu.ccfit.petrov.database.military_district.formation.service.PlatoonService;

@Controller
@RequiredArgsConstructor
public class PlatoonController {

  private final PlatoonService platoonService;

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_FORMATIONS')")
  public List<Platoon> getPlatoons(
      @Argument("filter") PlatoonFilter filter,
      @Argument("pagination") Pagination pagination,
      @Argument("sorts") List<Sorting> sorts) {
    return platoonService.getAll(filter, pagination, sorts);
  }

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_FORMATIONS')")
  public long getPlatoonCount(@Argument("filter") PlatoonFilter filter) {
    return platoonService.getAllCount(filter);
  }

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_FORMATIONS')")
  public Platoon getPlatoon(@Argument("name") @NonNull String name) {
    return platoonService.getByName(name);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_FORMATIONS')")
  public Platoon createPlatoon(@Argument("input") @Valid @NonNull PlatoonInput platoonInput) {
    return platoonService.create(platoonInput);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_FORMATIONS')")
  public Platoon updatePlatoon(
      @Argument("name") @NonNull String name,
      @Argument("input") @Valid @NonNull PlatoonInput platoonInput) {
    return platoonService.update(name, platoonInput);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_FORMATIONS')")
  public long deletePlatoon(@Argument("name") @NonNull String name) {
    return platoonService.delete(name);
  }
}
