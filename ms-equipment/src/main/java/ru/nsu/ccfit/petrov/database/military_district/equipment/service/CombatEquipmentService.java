package ru.nsu.ccfit.petrov.database.military_district.equipment.service;

import static ru.nsu.ccfit.petrov.database.military_district.equipment.util.SpecPageSortUtils.generatePageable;
import static ru.nsu.ccfit.petrov.database.military_district.equipment.util.SpecPageSortUtils.generateSort;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.ccfit.petrov.database.military_district.equipment.dto.CombatEquipmentDto;
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
public class CombatEquipmentService implements GraphQLService {

  private static final List<String> availableSortFields =
      List.of("serialNumber", "type.name", "type.category.name", "unit.name");

  private final CombatEquipmentRepository combatEquipmentRepository;
  private final CombatEquipmentTypeRepository combatEquipmentTypeRepository;
  private final CombatEquipmentMapper combatEquipmentMapper;

  public List<CombatEquipment> getAll(
      String type,
      String category,
      String unit,
      Integer page,
      Integer pageSize,
      String sortField,
      Boolean sortAsc) {
    var sort = generateSort(sortField, sortAsc, availableSortFields);
    var pageable = generatePageable(page, pageSize, sort);
    var spec = generateSpecification(type, category, unit);
    return combatEquipmentRepository.findAll(spec, pageable, sort);
  }

  public long getAllCount(String type, String category, String unit) {
    var spec = generateSpecification(type, category, unit);
    if (spec == null) {
      return combatEquipmentRepository.count();
    }
    return combatEquipmentRepository.count(spec);
  }

  public CombatEquipment getBySerialNumber(@NonNull String serialNumber) {
    return combatEquipmentRepository.findBySerialNumber(serialNumber).orElse(null);
  }

  @Transactional
  public CombatEquipment create(@Valid @NonNull CombatEquipmentDto combatEquipmentDto) {
    if (combatEquipmentRepository.existsBySerialNumber(combatEquipmentDto.getSerialNumber())) {
      throw new CombatEquipmentAlreadyExistsException();
    }

    var combatEquipment = combatEquipmentMapper.toEntity(combatEquipmentDto);
    if (combatEquipmentDto.getType() != null) {
      combatEquipment.setType(
          combatEquipmentTypeRepository
              .findByName(combatEquipmentDto.getType())
              .orElseThrow(CombatEquipmentTypeNotFoundException::new));
    }

    return combatEquipmentRepository.save(combatEquipment);
  }

  @Transactional
  public CombatEquipment update(
      @NonNull String serialNumber, @Valid @NonNull CombatEquipmentDto combatEquipmentDto) {
    var combatEquipment =
        combatEquipmentRepository
            .findBySerialNumber(serialNumber)
            .orElseThrow(CombatEquipmentNotFoundException::new);

    combatEquipmentMapper.partialUpdate(combatEquipmentDto, combatEquipment);
    if (combatEquipmentDto.getType() != null) {
      combatEquipment.setType(
          combatEquipmentTypeRepository
              .findByName(combatEquipmentDto.getType())
              .orElseThrow(CombatEquipmentTypeNotFoundException::new));
    }

    return combatEquipmentRepository.save(combatEquipment);
  }

  @Transactional
  public long delete(@NonNull String serialNumber) {
    return combatEquipmentRepository.deleteBySerialNumber(serialNumber);
  }

  private Specification<CombatEquipment> generateSpecification(
      String type, String category, String unit) {
    Specification<CombatEquipment> spec = null;
    if (Objects.nonNull(type)) {
      spec = (root, query, builder) -> builder.like(root.get("type").get("name"), "%" + type + "%");
    }
    if (Objects.nonNull(category)) {
      Specification<CombatEquipment> newSpec =
          (root, query, builder) ->
              builder.like(root.get("type").get("category").get("name"), "%" + category + "%");
      spec = Objects.isNull(spec) ? newSpec : spec.and(newSpec);
    }
    if (Objects.nonNull(unit)) {
      Specification<CombatEquipment> newSpec =
          (root, query, builder) -> builder.like(root.get("unit").get("name"), "%" + unit + "%");
      spec = Objects.isNull(spec) ? newSpec : spec.and(newSpec);
    }
    return spec;
  }

  @Override
  public Object resolveReference(@NonNull Map<String, Object> reference) {
    if (reference.get("serialNumber") instanceof String serialNumber) {
      return getBySerialNumber(serialNumber);
    }
    return null;
  }
}
