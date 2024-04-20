package ru.nsu.ccfit.petrov.database.military_district.infrastructure.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import ru.nsu.ccfit.petrov.database.military_district.infrastructure.dto.BuildingFilter;
import ru.nsu.ccfit.petrov.database.military_district.infrastructure.dto.BuildingInput;
import ru.nsu.ccfit.petrov.database.military_district.infrastructure.dto.Pagination;
import ru.nsu.ccfit.petrov.database.military_district.infrastructure.dto.Sorting;
import ru.nsu.ccfit.petrov.database.military_district.infrastructure.persistence.entity.Building;
import ru.nsu.ccfit.petrov.database.military_district.infrastructure.service.BuildingService;

@Controller
@RequiredArgsConstructor
public class BuildingController {

  private final BuildingService buildingService;

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_INFRASTRUCTURES')")
  public List<Building> getBuildings(
      @Argument("filter") BuildingFilter filter,
      @Argument("pagination") Pagination pagination,
      @Argument("sorts") List<Sorting> sorts) {
    return buildingService.getAll(filter, pagination, sorts);
  }

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_INFRASTRUCTURES')")
  public long getBuildingCount(@Argument("filter") BuildingFilter filter) {
    return buildingService.getAllCount(filter);
  }

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_INFRASTRUCTURES')")
  public Building getBuilding(
      @Argument("name") @NonNull String name, @Argument("unit") String unit) {
    return buildingService.getByNameAndUnit(name, unit);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_INFRASTRUCTURES')")
  public Building createBuilding(@Argument("input") @Valid @NonNull BuildingInput buildingInput) {
    return buildingService.create(buildingInput);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_INFRASTRUCTURES')")
  public Building updateBuilding(
      @Argument("name") @NonNull String name,
      @Argument("unit") String unit,
      @Argument("input") @Valid @NonNull BuildingInput buildingInput) {
    return buildingService.update(name, unit, buildingInput);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_INFRASTRUCTURES')")
  public long deleteBuilding(
      @Argument("name") @NonNull String name, @Argument("unit") String unit) {
    return buildingService.delete(name, unit);
  }
}
