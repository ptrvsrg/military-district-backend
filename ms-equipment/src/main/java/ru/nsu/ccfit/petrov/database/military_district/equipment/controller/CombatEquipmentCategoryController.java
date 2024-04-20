package ru.nsu.ccfit.petrov.database.military_district.equipment.controller;

import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import ru.nsu.ccfit.petrov.database.military_district.equipment.dto.Pagination;
import ru.nsu.ccfit.petrov.database.military_district.equipment.dto.Sorting;
import ru.nsu.ccfit.petrov.database.military_district.equipment.persistence.entity.CombatEquipmentCategory;
import ru.nsu.ccfit.petrov.database.military_district.equipment.service.CombatEquipmentCategoryService;

@Controller
@RequiredArgsConstructor
public class CombatEquipmentCategoryController {

  private final CombatEquipmentCategoryService combatEquipmentCategoryService;

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_EQUIPMENTS')")
  public List<CombatEquipmentCategory> getCombatEquipmentCategories(
      @Argument("pagination") Pagination pagination, @Argument("sorts") List<Sorting> sorts) {
    return combatEquipmentCategoryService.getAll(pagination, sorts);
  }

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_EQUIPMENTS')")
  public long getCombatEquipmentCategoryCount() {
    return combatEquipmentCategoryService.getAllCount();
  }

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_EQUIPMENTS')")
  public CombatEquipmentCategory getCombatEquipmentCategory(
      @Argument("name") @NonNull String name) {
    return combatEquipmentCategoryService.getByName(name);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_EQUIPMENTS')")
  public CombatEquipmentCategory createCombatEquipmentCategory(
      @Argument("input") @NonNull String category) {
    return combatEquipmentCategoryService.create(category);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_EQUIPMENTS')")
  public CombatEquipmentCategory updateCombatEquipmentCategory(
      @Argument("name") @NonNull String name, @Argument("input") @NonNull String category) {
    return combatEquipmentCategoryService.update(name, category);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_EQUIPMENTS')")
  public long deleteCombatEquipmentCategory(@Argument("name") @NonNull String name) {
    return combatEquipmentCategoryService.delete(name);
  }
}
