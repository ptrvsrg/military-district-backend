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
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.SquadDto;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.entity.Squad;
import ru.nsu.ccfit.petrov.database.military_district.formation.service.SquadService;

@Controller
@RequiredArgsConstructor
public class SquadController {

  private final SquadService squadService;

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_FORMATIONS')")
  public List<Squad> getSquads(
      @Argument("name") String name,
      @Argument("commander") String commander,
      @Argument("platoon") String platoon,
      @Argument("page") Integer page,
      @Argument("pageSize") Integer pageSize,
      @Argument("sort") String sortField,
      @Argument("sortAsc") Boolean sortAsc) {
    return squadService.getAll(name, commander, platoon, page, pageSize, sortField, sortAsc);
  }

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_FORMATIONS')")
  public long getSquadCount(
      @Argument("name") String name,
      @Argument("commander") String commander,
      @Argument("platoon") String platoon) {
    return squadService.getAllCount(name, commander, platoon);
  }

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_FORMATIONS')")
  public Squad getSquad(@Argument("name") @NonNull String name) {
    return squadService.getByName(name);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_FORMATIONS')")
  public Squad createSquad(@Argument("input") @Valid @NonNull SquadDto squadDto) {
    return squadService.create(squadDto);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_FORMATIONS')")
  public Squad updateSquad(
      @Argument("name") @NonNull String name,
      @Argument("input") @Valid @NonNull SquadDto squadDto) {
    return squadService.update(name, squadDto);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_FORMATIONS')")
  public long deleteSquad(@Argument("name") @NonNull String name) {
    return squadService.delete(name);
  }
}
