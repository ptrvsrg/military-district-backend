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
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.DivisionFilter;
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.DivisionInput;
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.Pagination;
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.Sorting;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.entity.Division;
import ru.nsu.ccfit.petrov.database.military_district.formation.service.DivisionService;

@Controller
@RequiredArgsConstructor
public class DivisionController {

  private final DivisionService divisionService;

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_FORMATIONS')")
  public List<Division> getDivisions(
      @Argument("filter") DivisionFilter filter,
      @Argument("pagination") Pagination pagination,
      @Argument("sorts") List<Sorting> sorts) {
    return divisionService.getAll(filter, pagination, sorts);
  }

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_FORMATIONS')")
  public long getDivisionCount(@Argument("filter") DivisionFilter filter) {
    return divisionService.getAllCount(filter);
  }

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_FORMATIONS')")
  public Division getDivision(@Argument("name") @NonNull String name) {
    return divisionService.getByName(name);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_FORMATIONS')")
  public Division createDivision(@Argument("input") @Valid @NonNull DivisionInput divisionInput) {
    return divisionService.create(divisionInput);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_FORMATIONS')")
  public Division updateDivision(
      @Argument("name") @NonNull String name,
      @Argument("input") @Valid @NonNull DivisionInput divisionInput) {
    return divisionService.update(name, divisionInput);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_FORMATIONS')")
  public long deleteDivision(@Argument("name") @NonNull String name) {
    return divisionService.delete(name);
  }
}
