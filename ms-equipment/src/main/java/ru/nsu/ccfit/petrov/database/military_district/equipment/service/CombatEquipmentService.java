package ru.nsu.ccfit.petrov.database.military_district.equipment.service;

import static ru.nsu.ccfit.petrov.database.military_district.equipment.util.SpecPageSortUtils.generateCombatEquipmentSpec;
import static ru.nsu.ccfit.petrov.database.military_district.equipment.util.SpecPageSortUtils.generatePageable;
import static ru.nsu.ccfit.petrov.database.military_district.equipment.util.SpecPageSortUtils.generateSort;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.ccfit.petrov.database.military_district.equipment.dto.CombatEquipmentFilter;
import ru.nsu.ccfit.petrov.database.military_district.equipment.dto.CombatEquipmentInput;
import ru.nsu.ccfit.petrov.database.military_district.equipment.dto.Pagination;
import ru.nsu.ccfit.petrov.database.military_district.equipment.dto.Sorting;
import ru.nsu.ccfit.petrov.database.military_district.equipment.exception.CombatEquipmentAlreadyExistsException;
import ru.nsu.ccfit.petrov.database.military_district.equipment.exception.CombatEquipmentNotFoundException;
import ru.nsu.ccfit.petrov.database.military_district.equipment.exception.CombatEquipmentTypeNotFoundException;
import ru.nsu.ccfit.petrov.database.military_district.equipment.mapper.CombatEquipmentMapper;
import ru.nsu.ccfit.petrov.database.military_district.equipment.persistence.entity.CombatEquipment;
import ru.nsu.ccfit.petrov.database.military_district.equipment.persistence.repository.CombatEquipmentRepository;
import ru.nsu.ccfit.petrov.database.military_district.equipment.persistence.repository.CombatEquipmentTypeRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CombatEquipmentService implements GraphQLService {

  private static final List<String> availableSortFields =
      List.of("serialNumber", "type.name", "type.category.name", "unit.name");

  private final CombatEquipmentRepository combatEquipmentRepository;
  private final CombatEquipmentTypeRepository combatEquipmentTypeRepository;
  private final CombatEquipmentMapper combatEquipmentMapper;

  public List<CombatEquipment> getAll(
      CombatEquipmentFilter filter, Pagination pagination, List<Sorting> sorts) {
    log.info(
        "Get all combat equipments: filter={}, pagination={}, sorts={}", filter, pagination, sorts);
    var sort = generateSort(sorts, availableSortFields);
    var pageable = generatePageable(pagination, sort);
    var spec = generateCombatEquipmentSpec(filter);
    return combatEquipmentRepository.findAll(spec, pageable, sort);
  }

  public long getAllCount(CombatEquipmentFilter filter) {
    log.info("Get all combat equipments count: filter={}", filter);
    var spec = generateCombatEquipmentSpec(filter);
    return combatEquipmentRepository.count(spec);
  }

  public CombatEquipment getBySerialNumber(@NonNull String serialNumber) {
    log.info("Get combat equipment by serial number: serialNumber={}", serialNumber);
    return combatEquipmentRepository.findBySerialNumber(serialNumber).orElse(null);
  }

  @Transactional
  public CombatEquipment create(@Valid @NonNull CombatEquipmentInput combatEquipmentInput) {
    log.info("Create combat equipment: input={}", combatEquipmentInput);
    if (combatEquipmentRepository.existsBySerialNumber(combatEquipmentInput.getSerialNumber())) {
      throw new CombatEquipmentAlreadyExistsException();
    }

    var combatEquipment = combatEquipmentMapper.toEntity(combatEquipmentInput);
    if (combatEquipmentInput.getType() != null) {
      combatEquipment.setType(
          combatEquipmentTypeRepository
              .findByName(combatEquipmentInput.getType())
              .orElseThrow(CombatEquipmentTypeNotFoundException::new));
    }

    return combatEquipmentRepository.save(combatEquipment);
  }

  @Transactional
  public CombatEquipment update(
      @NonNull String serialNumber, @Valid @NonNull CombatEquipmentInput combatEquipmentInput) {
    log.info(
        "Update combat equipment: serialNumber={}, input={}", serialNumber, combatEquipmentInput);
    var combatEquipment =
        combatEquipmentRepository
            .findBySerialNumber(serialNumber)
            .orElseThrow(CombatEquipmentNotFoundException::new);

    combatEquipmentMapper.partialUpdate(combatEquipmentInput, combatEquipment);
    if (combatEquipmentInput.getType() != null) {
      combatEquipment.setType(
          combatEquipmentTypeRepository
              .findByName(combatEquipmentInput.getType())
              .orElseThrow(CombatEquipmentTypeNotFoundException::new));
    }

    return combatEquipmentRepository.save(combatEquipment);
  }

  @Transactional
  public long delete(@NonNull String serialNumber) {
    log.info("Delete combat equipment: serialNumber={}", serialNumber);
    return combatEquipmentRepository.deleteBySerialNumber(serialNumber);
  }

  @Override
  public Object resolveReference(@NonNull Map<String, Object> reference) {
    log.info("Resolve combat equipment reference: reference={}", reference);
    if (reference.get("serialNumber") instanceof String serialNumber) {
      return getBySerialNumber(serialNumber);
    }
    return null;
  }
}
