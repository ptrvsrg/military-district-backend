package ru.nsu.ccfit.petrov.database.military_district.weapon.controller;

import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import ru.nsu.ccfit.petrov.database.military_district.weapon.persistence.entity.WeaponCategory;
import ru.nsu.ccfit.petrov.database.military_district.weapon.service.WeaponCategoryService;

@Controller
@RequiredArgsConstructor
public class WeaponCategoryController {

  private final WeaponCategoryService weaponCategoryService;

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_WEAPONS')")
  public List<WeaponCategory> getWeaponCategories(
      @Argument("page") Integer page,
      @Argument("pageSize") Integer pageSize,
      @Argument("sort") String sortField,
      @Argument("sortAsc") Boolean sortAsc) {
    return weaponCategoryService.getAll(page, pageSize, sortField, sortAsc);
  }

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_WEAPONS')")
  public long getWeaponCategoryCount() {
    return weaponCategoryService.getAllCount();
  }

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_WEAPONS')")
  public WeaponCategory getWeaponCategory(@Argument("name") @NonNull String name) {
    return weaponCategoryService.getByName(name);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_WEAPONS')")
  public WeaponCategory createWeaponCategory(@Argument("input") @NonNull String category) {
    return weaponCategoryService.create(category);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_WEAPONS')")
  public WeaponCategory updateWeaponCategory(
      @Argument("name") @NonNull String name, @Argument("input") @NonNull String category) {
    return weaponCategoryService.update(name, category);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_WEAPONS')")
  public long deleteWeaponCategory(@Argument("name") @NonNull String name) {
    return weaponCategoryService.delete(name);
  }
}
