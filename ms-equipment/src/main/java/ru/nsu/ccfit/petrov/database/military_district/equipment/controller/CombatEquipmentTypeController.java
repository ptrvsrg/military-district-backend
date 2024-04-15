package ru.nsu.ccfit.petrov.database.military_district.equipment.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import ru.nsu.ccfit.petrov.database.military_district.equipment.dto.CombatEquipmentTypeDto;
import ru.nsu.ccfit.petrov.database.military_district.equipment.persistence.entity.CombatEquipmentType;
import ru.nsu.ccfit.petrov.database.military_district.equipment.service.CombatEquipmentTypeService;

@Controller
@RequiredArgsConstructor
public class CombatEquipmentTypeController {

  private final CombatEquipmentTypeService combatEquipmentTypeService;

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_EQUIPMENTS')")
  public List<CombatEquipmentType> getCombatEquipmentTypes(
      @Argument("name") String name,
      @Argument("category") String category,
      @Argument("page") Integer page,
      @Argument("pageSize") Integer pageSize,
      @Argument("sort") String sortField,
      @Argument("sortAsc") Boolean sortAsc) {
    return combatEquipmentTypeService.getAll(name, category, page, pageSize, sortField, sortAsc);
  }

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_EQUIPMENTS')")
  public long getCombatEquipmentTypeCount(
      @Argument("name") String name, @Argument("category") String category) {
    return combatEquipmentTypeService.getAllCount(name, category);
  }

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_EQUIPMENTS')")
  public CombatEquipmentType getCombatEquipmentType(
      @Argument("name") @NonNull String name, @Argument("category") @NonNull String category) {
    return combatEquipmentTypeService.getByNameAndCategory(name, category);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_EQUIPMENTS')")
  public CombatEquipmentType createCombatEquipmentType(
      @Argument("input") @Valid @NonNull CombatEquipmentTypeDto typeDto) {
    return combatEquipmentTypeService.create(typeDto);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_EQUIPMENTS')")
  public CombatEquipmentType updateCombatEquipmentType(
      @Argument("name") @NonNull String name,
      @Argument("category") @NonNull String category,
      @Argument("input") @Valid @NonNull CombatEquipmentTypeDto typeDto) {
    return combatEquipmentTypeService.update(name, category, typeDto);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_EQUIPMENTS')")
  public long deleteCombatEquipmentType(
      @Argument("name") @NonNull String name, @Argument("category") @NonNull String category) {
    return combatEquipmentTypeService.delete(name, category);
  }
}
