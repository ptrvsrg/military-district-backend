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
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.ArmyFilter;
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.ArmyInput;
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.Pagination;
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.Sorting;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.entity.Army;
import ru.nsu.ccfit.petrov.database.military_district.formation.service.ArmyService;

@Controller
@RequiredArgsConstructor
public class ArmyController {

  private final ArmyService armyService;

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_FORMATIONS')")
  public List<Army> getArmies(
      @Argument("filter") ArmyFilter filter,
      @Argument("pagination") Pagination pagination,
      @Argument("sorts") List<Sorting> sorts) {
    return armyService.getAll(filter, pagination, sorts);
  }

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_FORMATIONS')")
  public long getArmyCount(@Argument("filter") ArmyFilter filter) {
    return armyService.getAllCount(filter);
  }

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_FORMATIONS')")
  public Army getArmy(@Argument("name") @NonNull String name) {
    return armyService.getByName(name);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_FORMATIONS')")
  public Army createArmy(@Argument("input") @Valid @NonNull ArmyInput armyInput) {
    return armyService.create(armyInput);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_FORMATIONS')")
  public Army updateArmy(
      @Argument("name") @NonNull String name,
      @Argument("input") @Valid @NonNull ArmyInput armyInput) {
    return armyService.update(name, armyInput);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_FORMATIONS')")
  public long deleteArmy(@Argument("name") @NonNull String name) {
    return armyService.delete(name);
  }
}
