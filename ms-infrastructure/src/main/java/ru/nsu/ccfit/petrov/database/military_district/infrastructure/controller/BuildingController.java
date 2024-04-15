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
import ru.nsu.ccfit.petrov.database.military_district.infrastructure.dto.BuildingDto;
import ru.nsu.ccfit.petrov.database.military_district.infrastructure.persistence.entity.Building;
import ru.nsu.ccfit.petrov.database.military_district.infrastructure.service.BuildingService;

@Controller
@RequiredArgsConstructor
public class BuildingController {

  private final BuildingService buildingService;

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_INFRASTRUCTURES')")
  public List<Building> getBuildings(
      @Argument("name") String name,
      @Argument("address") String address,
      @Argument("unit") String unit,
      @Argument("page") Integer page,
      @Argument("pageSize") Integer pageSize,
      @Argument("sort") String sortField,
      @Argument("sortAsc") Boolean sortAsc) {
    return buildingService.getAll(name, address, unit, page, pageSize, sortField, sortAsc);
  }

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_INFRASTRUCTURES')")
  public long getBuildingCount(
      @Argument("name") String name,
      @Argument("address") String address,
      @Argument("unit") String unit) {
    return buildingService.getAllCount(name, address, unit);
  }

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_INFRASTRUCTURES')")
  public Building getBuilding(
      @Argument("name") @NonNull String name, @Argument("unit") String unit) {
    return buildingService.getByNameAndUnit(name, unit);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_INFRASTRUCTURES')")
  public Building createBuilding(@Argument("input") @Valid @NonNull BuildingDto buildingDto) {
    return buildingService.create(buildingDto);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_INFRASTRUCTURES')")
  public Building updateBuilding(
      @Argument("name") @NonNull String name,
      @Argument("unit") String unit,
      @Argument("input") @Valid @NonNull BuildingDto buildingDto) {
    return buildingService.update(name, unit, buildingDto);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_INFRASTRUCTURES')")
  public long deleteBuilding(
      @Argument("name") @NonNull String name, @Argument("unit") String unit) {
    return buildingService.delete(name, unit);
  }
}
