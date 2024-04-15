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
import ru.nsu.ccfit.petrov.database.military_district.weapon.dto.WeaponDto;
import ru.nsu.ccfit.petrov.database.military_district.weapon.persistence.entity.Weapon;
import ru.nsu.ccfit.petrov.database.military_district.weapon.service.WeaponService;

@Controller
@RequiredArgsConstructor
public class WeaponController {

  private final WeaponService weaponService;

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_WEAPONS')")
  public List<Weapon> getWeapons(
      @Argument("type") String type,
      @Argument("category") String category,
      @Argument("unit") String unit,
      @Argument("page") Integer page,
      @Argument("pageSize") Integer pageSize,
      @Argument("sort") String sortField,
      @Argument("sortAsc") Boolean sortAsc) {
    return weaponService.getAll(type, category, unit, page, pageSize, sortField, sortAsc);
  }

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_WEAPONS')")
  public long getWeaponCount(
      @Argument("type") String type,
      @Argument("category") String category,
      @Argument("unit") String unit) {
    return weaponService.getAllCount(type, category, unit);
  }

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_WEAPONS')")
  public Weapon getWeapon(@Argument("serialNumber") @NonNull String serialNumber) {
    return weaponService.getBySerialNumber(serialNumber);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_WEAPONS')")
  public Weapon createWeapon(@Argument("input") @Valid @NonNull WeaponDto weaponDto) {
    return weaponService.create(weaponDto);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_WEAPONS')")
  public Weapon updateWeapon(
      @Argument("serialNumber") @NonNull String serialNumber,
      @Argument("input") @Valid @NonNull WeaponDto weaponDto) {
    return weaponService.update(serialNumber, weaponDto);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_WEAPONS')")
  public long deleteWeapon(@Argument("serialNumber") @NonNull String serialNumber) {
    return weaponService.delete(serialNumber);
  }
}
