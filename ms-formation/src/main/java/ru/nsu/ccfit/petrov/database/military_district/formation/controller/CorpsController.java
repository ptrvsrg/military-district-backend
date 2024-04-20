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
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.CorpsFilter;
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.CorpsInput;
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.Pagination;
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.Sorting;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.entity.Corps;
import ru.nsu.ccfit.petrov.database.military_district.formation.service.CorpsService;

@Controller
@RequiredArgsConstructor
public class CorpsController {

  private final CorpsService corpsService;

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_FORMATIONS')")
  public List<Corps> getCorps(
      @Argument("filter") CorpsFilter filter,
      @Argument("pagination") Pagination pagination,
      @Argument("sorts") List<Sorting> sorts) {
    return corpsService.getAll(filter, pagination, sorts);
  }

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_FORMATIONS')")
  public long getCorpsCount(@Argument("filter") CorpsFilter filter) {
    return corpsService.getAllCount(filter);
  }

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_FORMATIONS')")
  public Corps getOneCorps(@Argument("name") @NonNull String name) {
    return corpsService.getByName(name);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_FORMATIONS')")
  public Corps createCorps(@Argument("input") @Valid @NonNull CorpsInput corpsInput) {
    return corpsService.create(corpsInput);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_FORMATIONS')")
  public Corps updateCorps(
      @Argument("name") @NonNull String name,
      @Argument("input") @Valid @NonNull CorpsInput corpsInput) {
    return corpsService.update(name, corpsInput);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_FORMATIONS')")
  public long deleteCorps(@Argument("name") @NonNull String name) {
    return corpsService.delete(name);
  }
}
