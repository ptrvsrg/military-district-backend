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
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.UnitDto;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.entity.Unit;
import ru.nsu.ccfit.petrov.database.military_district.formation.service.UnitService;

@Controller
@RequiredArgsConstructor
public class UnitController {

  private final UnitService unitService;

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_FORMATIONS')")
  public List<Unit> getUnits(
      @Argument("name") String name,
      @Argument("address") String address,
      @Argument("commander") String commander,
      @Argument("page") Integer page,
      @Argument("pageSize") Integer pageSize,
      @Argument("sort") String sortField,
      @Argument("sortAsc") Boolean sortAsc) {
    return unitService.getAll(name, address, commander, page, pageSize, sortField, sortAsc);
  }

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_FORMATIONS')")
  public long getUnitCount(
      @Argument("name") String name,
      @Argument("commander") String commander,
      @Argument("address") String address) {
    return unitService.getAllCount(name, address, commander);
  }

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_FORMATIONS')")
  public Unit getUnit(@Argument("name") @NonNull String name) {
    return unitService.getByName(name);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_FORMATIONS')")
  public Unit createUnit(@Argument("input") @Valid @NonNull UnitDto unitDto) {
    return unitService.create(unitDto);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_FORMATIONS')")
  public Unit updateUnit(
      @Argument("name") @NonNull String name, @Argument("input") @Valid @NonNull UnitDto unitDto) {
    return unitService.update(name, unitDto);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_FORMATIONS')")
  public long deleteUnit(@Argument("name") String name) {
    return unitService.delete(name);
  }
}
