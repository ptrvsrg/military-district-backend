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
import ru.nsu.ccfit.petrov.database.military_district.weapon.dto.WeaponFilter;
import ru.nsu.ccfit.petrov.database.military_district.weapon.dto.WeaponInput;
import ru.nsu.ccfit.petrov.database.military_district.weapon.persistence.entity.Weapon;
import ru.nsu.ccfit.petrov.database.military_district.weapon.service.WeaponService;

@Controller
@RequiredArgsConstructor
public class WeaponController {

  private final WeaponService weaponService;

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_WEAPONS')")
  public List<Weapon> getWeapons(
      @Argument("filter") WeaponFilter filter,
      @Argument("pagination") Pagination pagination,
      @Argument("sorts") List<Sorting> sorts) {
    return weaponService.getAll(filter, pagination, sorts);
  }

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_WEAPONS')")
  public long getWeaponCount(@Argument("filter") WeaponFilter filter) {
    return weaponService.getAllCount(filter);
  }

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_WEAPONS')")
  public Weapon getWeapon(@Argument("serialNumber") @NonNull String serialNumber) {
    return weaponService.getBySerialNumber(serialNumber);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_WEAPONS')")
  public Weapon createWeapon(@Argument("input") @Valid @NonNull WeaponInput weaponInput) {
    return weaponService.create(weaponInput);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_WEAPONS')")
  public Weapon updateWeapon(
      @Argument("serialNumber") @NonNull String serialNumber,
      @Argument("input") @Valid @NonNull WeaponInput weaponInput) {
    return weaponService.update(serialNumber, weaponInput);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_WEAPONS')")
  public long deleteWeapon(@Argument("serialNumber") @NonNull String serialNumber) {
    return weaponService.delete(serialNumber);
  }
}
