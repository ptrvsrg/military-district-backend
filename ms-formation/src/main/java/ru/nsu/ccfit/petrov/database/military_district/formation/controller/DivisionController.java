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
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.DivisionDto;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.entity.Division;
import ru.nsu.ccfit.petrov.database.military_district.formation.service.DivisionService;

@Controller
@RequiredArgsConstructor
public class DivisionController {

  private final DivisionService divisionService;

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_FORMATIONS')")
  public List<Division> getDivisions(
      @Argument("name") String name,
      @Argument("commander") String commander,
      @Argument("page") Integer page,
      @Argument("pageSize") Integer pageSize,
      @Argument("sort") String sortField,
      @Argument("sortAsc") Boolean sortAsc) {
    return divisionService.getAll(name, commander, page, pageSize, sortField, sortAsc);
  }

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_FORMATIONS')")
  public long getDivisionCount(
      @Argument("name") String name, @Argument("commander") String commander) {
    return divisionService.getAllCount(name, commander);
  }

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_FORMATIONS')")
  public Division getDivision(@Argument("name") @NonNull String name) {
    return divisionService.getByName(name);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_FORMATIONS')")
  public Division createDivision(@Argument("input") @Valid @NonNull DivisionDto divisionDto) {
    return divisionService.create(divisionDto);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_FORMATIONS')")
  public Division updateDivision(
      @Argument("name") @NonNull String name,
      @Argument("input") @Valid @NonNull DivisionDto divisionDto) {
    return divisionService.update(name, divisionDto);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_FORMATIONS')")
  public long deleteDivision(@Argument("name") @NonNull String name) {
    return divisionService.delete(name);
  }
}
