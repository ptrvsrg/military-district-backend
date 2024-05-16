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
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.SquadFilter;
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.SquadInput;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.entity.Squad;
import ru.nsu.ccfit.petrov.database.military_district.formation.service.SquadService;

@Controller
@RequiredArgsConstructor
public class SquadController {

  private final SquadService squadService;

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_FORMATIONS')")
  public List<Squad> getSquads(
      @Argument("filter") SquadFilter filter,
      @Argument("pagination") Pagination pagination,
      @Argument("sorts") List<Sorting> sorts) {
    return squadService.getAll(filter, pagination, sorts);
  }

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_FORMATIONS')")
  public long getSquadCount(@Argument("filter") SquadFilter filter) {
    return squadService.getAllCount(filter);
  }

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_FORMATIONS')")
  public Squad getSquad(@Argument("name") @NonNull String name) {
    return squadService.getByName(name);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_FORMATIONS')")
  public Squad createSquad(@Argument("input") @Valid @NonNull SquadInput squadInput) {
    return squadService.create(squadInput);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_FORMATIONS')")
  public Squad updateSquad(
      @Argument("name") @NonNull String name,
      @Argument("input") @Valid @NonNull SquadInput squadInput) {
    return squadService.update(name, squadInput);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_FORMATIONS')")
  public long deleteSquad(@Argument("name") @NonNull String name) {
    return squadService.delete(name);
  }
}
