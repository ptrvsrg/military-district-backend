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
import ru.nsu.ccfit.petrov.database.military_district.weapon.dto.Pagination;
import ru.nsu.ccfit.petrov.database.military_district.weapon.dto.Sorting;
import ru.nsu.ccfit.petrov.database.military_district.weapon.dto.WeaponTypeFilter;
import ru.nsu.ccfit.petrov.database.military_district.weapon.dto.WeaponTypeInput;
import ru.nsu.ccfit.petrov.database.military_district.weapon.persistence.entity.WeaponType;
import ru.nsu.ccfit.petrov.database.military_district.weapon.service.WeaponTypeService;

@Controller
@RequiredArgsConstructor
public class WeaponTypeController {

  private final WeaponTypeService weaponTypeService;

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_WEAPONS')")
  public List<WeaponType> getWeaponTypes(
      @Argument("filter") WeaponTypeFilter filter,
      @Argument("pagination") Pagination pagination,
      @Argument("sorts") List<Sorting> sorts) {
    return weaponTypeService.getAll(filter, pagination, sorts);
  }

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_WEAPONS')")
  public long getWeaponTypeCount(@Argument("filter") WeaponTypeFilter filter) {
    return weaponTypeService.getAllCount(filter);
  }

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_WEAPONS')")
  public WeaponType getWeaponType(
      @Argument("name") @NonNull String name, @Argument("category") @NonNull String category) {
    return weaponTypeService.getByNameAndCategory(name, category);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_WEAPONS')")
  public WeaponType createWeaponType(@Argument("input") @Valid @NonNull WeaponTypeInput typeDto) {
    return weaponTypeService.create(typeDto);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_WEAPONS')")
  public WeaponType updateWeaponType(
      @Argument("name") @NonNull String name,
      @Argument("category") @NonNull String category,
      @Argument("input") @Valid @NonNull WeaponTypeInput typeDto) {
    return weaponTypeService.update(name, category, typeDto);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_WEAPONS')")
  public long deleteWeaponType(
      @Argument("name") @NonNull String name, @Argument("category") @NonNull String category) {
    return weaponTypeService.delete(name, category);
  }
}
