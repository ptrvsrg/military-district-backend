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
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.ArmyDto;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.entity.Army;
import ru.nsu.ccfit.petrov.database.military_district.formation.service.ArmyService;

@Controller
@RequiredArgsConstructor
public class ArmyController {

  private final ArmyService armyService;

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_FORMATIONS')")
  public List<Army> getArmies(
      @Argument("name") String name,
      @Argument("commander") String commander,
      @Argument("page") Integer page,
      @Argument("pageSize") Integer pageSize,
      @Argument("sort") String sortField,
      @Argument("sortAsc") Boolean sortAsc) {
    return armyService.getAll(name, commander, page, pageSize, sortField, sortAsc);
  }

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_FORMATIONS')")
  public long getArmyCount(@Argument("name") String name, @Argument("commander") String commander) {
    return armyService.getAllCount(name, commander);
  }

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_FORMATIONS')")
  public Army getArmy(@Argument("name") @NonNull String name) {
    return armyService.getByName(name);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_FORMATIONS')")
  public Army createArmy(@Argument("input") @Valid @NonNull ArmyDto armyDto) {
    return armyService.create(armyDto);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_FORMATIONS')")
  public Army updateArmy(
      @Argument("name") @NonNull String name, @Argument("input") @Valid @NonNull ArmyDto armyDto) {
    return armyService.update(name, armyDto);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_FORMATIONS')")
  public long deleteArmy(@Argument("name") @NonNull String name) {
    return armyService.delete(name);
  }
}
