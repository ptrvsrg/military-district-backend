package ru.nsu.ccfit.petrov.database.military_district.equipment.service;

import static ru.nsu.ccfit.petrov.database.military_district.equipment.util.SpecPageSortUtils.generatePageable;
import static ru.nsu.ccfit.petrov.database.military_district.equipment.util.SpecPageSortUtils.generateSort;

import java.util.List;
import java.util.Map;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.ccfit.petrov.database.military_district.equipment.exception.CombatEquipmentCategoryAlreadyExistsException;
import ru.nsu.ccfit.petrov.database.military_district.equipment.exception.CombatEquipmentCategoryNotFoundException;
import ru.nsu.ccfit.petrov.database.military_district.equipment.persistence.entity.CombatEquipmentCategory;
import ru.nsu.ccfit.petrov.database.military_district.equipment.persistence.repository.CombatEquipmentCategoryRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CombatEquipmentCategoryService implements GraphQLService {

  private static final List<String> availableSortFields = List.of("name");

  private final CombatEquipmentCategoryRepository combatEquipmentCategoryRepository;

  public List<CombatEquipmentCategory> getAll(
      Integer page, Integer pageSize, String sortField, Boolean sortAsc) {
    var sort = generateSort(sortField, sortAsc, availableSortFields);
    var pageable = generatePageable(page, pageSize, sort);
    return combatEquipmentCategoryRepository.findAll(null, pageable, sort);
  }

  public long getAllCount() {
    return combatEquipmentCategoryRepository.count();
  }

  public CombatEquipmentCategory getByName(@NonNull String name) {
    return combatEquipmentCategoryRepository.findByName(name).orElse(null);
  }

  @Transactional
  public CombatEquipmentCategory create(@NonNull String category) {
    if (combatEquipmentCategoryRepository.existsByName(category)) {
      throw new CombatEquipmentCategoryAlreadyExistsException();
    }

    var entity = new CombatEquipmentCategory();
    entity.setName(category);

    return combatEquipmentCategoryRepository.save(entity);
  }

  @Transactional
  public CombatEquipmentCategory update(@NonNull String name, @NonNull String category) {
    var entity =
        combatEquipmentCategoryRepository
            .findByName(name)
            .orElseThrow(CombatEquipmentCategoryNotFoundException::new);

    if (!name.equals(category) && combatEquipmentCategoryRepository.existsByName(category)) {
      throw new CombatEquipmentCategoryAlreadyExistsException();
    }

    entity.setName(category);
    return combatEquipmentCategoryRepository.save(entity);
  }

  @Transactional
  public long delete(@NonNull String name) {
    return combatEquipmentCategoryRepository.deleteByName(name);
  }

  @Override
  public CombatEquipmentCategory resolveReference(@NonNull Map<String, Object> reference) {
    if (reference.get("name") instanceof String name) {
      return getByName(name);
    }
    return null;
  }
}
