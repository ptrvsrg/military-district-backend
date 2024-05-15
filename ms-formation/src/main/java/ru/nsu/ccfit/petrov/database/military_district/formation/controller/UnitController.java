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
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.Sorting;
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.UnitFilter;
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.UnitInput;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.entity.Unit;
import ru.nsu.ccfit.petrov.database.military_district.formation.service.UnitService;

@Controller
@RequiredArgsConstructor
public class UnitController {

  private final UnitService unitService;

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_FORMATIONS')")
  public List<Unit> getUnits(
      @Argument("filter") UnitFilter filter,
      @Argument("pagination") Pagination pagination,
      @Argument("sorts") List<Sorting> sorts) {
    return unitService.getAll(filter, pagination, sorts);
  }

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_FORMATIONS')")
  public long getUnitCount(@Argument("filter") UnitFilter filter) {
    return unitService.getAllCount(filter);
  }

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_FORMATIONS')")
  public Unit getUnit(@Argument("name") @NonNull String name) {
    return unitService.getByName(name);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_FORMATIONS')")
  public Unit createUnit(@Argument("input") @Valid @NonNull UnitInput unitInput) {
    return unitService.create(unitInput);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_FORMATIONS')")
  public Unit updateUnit(
      @Argument("name") @NonNull String name,
      @Argument("input") @Valid @NonNull UnitInput unitInput) {
    return unitService.update(name, unitInput);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_FORMATIONS')")
  public long deleteUnit(@Argument("name") String name) {
    return unitService.delete(name);
  }
}
