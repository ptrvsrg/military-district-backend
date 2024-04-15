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
import ru.nsu.ccfit.petrov.database.military_district.equipment.dto.CombatEquipmentDto;
import ru.nsu.ccfit.petrov.database.military_district.equipment.persistence.entity.CombatEquipment;
import ru.nsu.ccfit.petrov.database.military_district.equipment.service.CombatEquipmentService;

@Controller
@RequiredArgsConstructor
public class CombatEquipmentController {

  private final CombatEquipmentService combatEquipmentService;

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_EQUIPMENTS')")
  public List<CombatEquipment> getCombatEquipments(
      @Argument("type") String type,
      @Argument("category") String category,
      @Argument("unit") String unit,
      @Argument("page") Integer page,
      @Argument("pageSize") Integer pageSize,
      @Argument("sort") String sortField,
      @Argument("sortAsc") Boolean sortAsc) {
    return combatEquipmentService.getAll(type, category, unit, page, pageSize, sortField, sortAsc);
  }

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_EQUIPMENTS')")
  public long getCombatEquipmentCount(
      @Argument("type") String type,
      @Argument("category") String category,
      @Argument("unit") String unit) {
    return combatEquipmentService.getAllCount(type, category, unit);
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
      @Argument("input") @Valid @NonNull CombatEquipmentDto combatEquipmentDto) {
    return combatEquipmentService.create(combatEquipmentDto);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_EQUIPMENTS')")
  public CombatEquipment updateCombatEquipment(
      @Argument("serialNumber") @NonNull String serialNumber,
      @Argument("input") @Valid @NonNull CombatEquipmentDto combatEquipmentDto) {
    return combatEquipmentService.update(serialNumber, combatEquipmentDto);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_EQUIPMENTS')")
  public long deleteCombatEquipment(@Argument("serialNumber") @NonNull String serialNumber) {
    return combatEquipmentService.delete(serialNumber);
  }
}
