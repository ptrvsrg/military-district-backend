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
import ru.nsu.ccfit.petrov.database.military_district.equipment.dto.CombatEquipmentFilter;
import ru.nsu.ccfit.petrov.database.military_district.equipment.dto.CombatEquipmentInput;
import ru.nsu.ccfit.petrov.database.military_district.equipment.dto.Pagination;
import ru.nsu.ccfit.petrov.database.military_district.equipment.dto.Sorting;
import ru.nsu.ccfit.petrov.database.military_district.equipment.persistence.entity.CombatEquipment;
import ru.nsu.ccfit.petrov.database.military_district.equipment.service.CombatEquipmentService;

@Controller
@RequiredArgsConstructor
public class CombatEquipmentController {

  private final CombatEquipmentService combatEquipmentService;

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_EQUIPMENTS')")
  public List<CombatEquipment> getCombatEquipments(
      @Argument("filter") CombatEquipmentFilter filter,
      @Argument("pagination") Pagination pagination,
      @Argument("sorts") List<Sorting> sorts) {
    return combatEquipmentService.getAll(filter, pagination, sorts);
  }

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_EQUIPMENTS')")
  public long getCombatEquipmentCount(@Argument("filter") CombatEquipmentFilter filter) {
    return combatEquipmentService.getAllCount(filter);
  }

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_EQUIPMENTS')")
  public CombatEquipment getCombatEquipment(
      @Argument("serialNumber") @NonNull String serialNumber) {
    return combatEquipmentService.getBySerialNumber(serialNumber);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_EQUIPMENTS')")
  public CombatEquipment createCombatEquipment(
      @Argument("input") @Valid @NonNull CombatEquipmentInput combatEquipmentInput) {
    return combatEquipmentService.create(combatEquipmentInput);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_EQUIPMENTS')")
  public CombatEquipment updateCombatEquipment(
      @Argument("serialNumber") @NonNull String serialNumber,
      @Argument("input") @Valid @NonNull CombatEquipmentInput combatEquipmentInput) {
    return combatEquipmentService.update(serialNumber, combatEquipmentInput);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_EQUIPMENTS')")
  public long deleteCombatEquipment(@Argument("serialNumber") @NonNull String serialNumber) {
    return combatEquipmentService.delete(serialNumber);
  }
}
