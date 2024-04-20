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
import ru.nsu.ccfit.petrov.database.military_district.equipment.dto.CombatEquipmentTypeFilter;
import ru.nsu.ccfit.petrov.database.military_district.equipment.dto.CombatEquipmentTypeInput;
import ru.nsu.ccfit.petrov.database.military_district.equipment.dto.Pagination;
import ru.nsu.ccfit.petrov.database.military_district.equipment.dto.Sorting;
import ru.nsu.ccfit.petrov.database.military_district.equipment.persistence.entity.CombatEquipmentType;
import ru.nsu.ccfit.petrov.database.military_district.equipment.service.CombatEquipmentTypeService;

@Controller
@RequiredArgsConstructor
public class CombatEquipmentTypeController {

  private final CombatEquipmentTypeService combatEquipmentTypeService;

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_EQUIPMENTS')")
  public List<CombatEquipmentType> getCombatEquipmentTypes(
      @Argument("filter") CombatEquipmentTypeFilter filter,
      @Argument("pagination") Pagination pagination,
      @Argument("sorts") List<Sorting> sorts) {
    return combatEquipmentTypeService.getAll(filter, pagination, sorts);
  }

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_EQUIPMENTS')")
  public long getCombatEquipmentTypeCount(@Argument("filter") CombatEquipmentTypeFilter filter) {
    return combatEquipmentTypeService.getAllCount(filter);
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
      @Argument("input") @Valid @NonNull CombatEquipmentTypeInput typeDto) {
    return combatEquipmentTypeService.create(typeDto);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_EQUIPMENTS')")
  public CombatEquipmentType updateCombatEquipmentType(
      @Argument("name") @NonNull String name,
      @Argument("category") @NonNull String category,
      @Argument("input") @Valid @NonNull CombatEquipmentTypeInput typeDto) {
    return combatEquipmentTypeService.update(name, category, typeDto);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_EQUIPMENTS')")
  public long deleteCombatEquipmentType(
      @Argument("name") @NonNull String name, @Argument("category") @NonNull String category) {
    return combatEquipmentTypeService.delete(name, category);
  }
}
