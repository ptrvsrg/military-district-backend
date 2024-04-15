package ru.nsu.ccfit.petrov.database.military_district.weapon.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import ru.nsu.ccfit.petrov.database.military_district.weapon.dto.WeaponTypeDto;
import ru.nsu.ccfit.petrov.database.military_district.weapon.persistence.entity.WeaponType;
import ru.nsu.ccfit.petrov.database.military_district.weapon.service.WeaponTypeService;

@Controller
@RequiredArgsConstructor
public class WeaponTypeController {

  private final WeaponTypeService weaponTypeService;

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_WEAPONS')")
  public List<WeaponType> getWeaponTypes(
      @Argument("name") String name,
      @Argument("category") String category,
      @Argument("page") Integer page,
      @Argument("pageSize") Integer pageSize,
      @Argument("sort") String sortField,
      @Argument("sortAsc") Boolean sortAsc) {
    return weaponTypeService.getAll(name, category, page, pageSize, sortField, sortAsc);
  }

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_WEAPONS')")
  public long getWeaponTypeCount(
      @Argument("name") String name, @Argument("category") String category) {
    return weaponTypeService.getAllCount(name, category);
  }

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_WEAPONS')")
  public WeaponType getWeaponType(
      @Argument("name") @NonNull String name, @Argument("category") @NonNull String category) {
    return weaponTypeService.getByNameAndCategory(name, category);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_WEAPONS')")
  public WeaponType createWeaponType(@Argument("input") @Valid @NonNull WeaponTypeDto typeDto) {
    return weaponTypeService.create(typeDto);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_WEAPONS')")
  public WeaponType updateWeaponType(
      @Argument("name") @NonNull String name,
      @Argument("category") @NonNull String category,
      @Argument("input") @Valid @NonNull WeaponTypeDto typeDto) {
    return weaponTypeService.update(name, category, typeDto);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_WEAPONS')")
  public long deleteWeaponType(
      @Argument("name") @NonNull String name, @Argument("category") @NonNull String category) {
    return weaponTypeService.delete(name, category);
  }
}
