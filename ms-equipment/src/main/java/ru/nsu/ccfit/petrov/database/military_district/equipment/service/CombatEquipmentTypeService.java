package ru.nsu.ccfit.petrov.database.military_district.equipment.service;

import static ru.nsu.ccfit.petrov.database.military_district.equipment.util.SpecPageSortUtils.generateCombatEquipmentTypeSpec;
import static ru.nsu.ccfit.petrov.database.military_district.equipment.util.SpecPageSortUtils.generatePageable;
import static ru.nsu.ccfit.petrov.database.military_district.equipment.util.SpecPageSortUtils.generateSort;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.ccfit.petrov.database.military_district.equipment.dto.AttributeInput;
import ru.nsu.ccfit.petrov.database.military_district.equipment.dto.CombatEquipmentTypeFilter;
import ru.nsu.ccfit.petrov.database.military_district.equipment.dto.CombatEquipmentTypeInput;
import ru.nsu.ccfit.petrov.database.military_district.equipment.dto.Pagination;
import ru.nsu.ccfit.petrov.database.military_district.equipment.dto.Sorting;
import ru.nsu.ccfit.petrov.database.military_district.equipment.exception.CombatEquipmentCategoryNotFoundException;
import ru.nsu.ccfit.petrov.database.military_district.equipment.exception.CombatEquipmentTypeAlreadyExistsException;
import ru.nsu.ccfit.petrov.database.military_district.equipment.exception.CombatEquipmentTypeNotFoundException;
import ru.nsu.ccfit.petrov.database.military_district.equipment.mapper.AttributeMapper;
import ru.nsu.ccfit.petrov.database.military_district.equipment.mapper.CombatEquipmentTypeMapper;
import ru.nsu.ccfit.petrov.database.military_district.equipment.persistence.entity.CombatEquipmentAttribute;
import ru.nsu.ccfit.petrov.database.military_district.equipment.persistence.entity.CombatEquipmentType;
import ru.nsu.ccfit.petrov.database.military_district.equipment.persistence.repository.CombatEquipmentAttributeRepository;
import ru.nsu.ccfit.petrov.database.military_district.equipment.persistence.repository.CombatEquipmentCategoryRepository;
import ru.nsu.ccfit.petrov.database.military_district.equipment.persistence.repository.CombatEquipmentTypeRepository;

@Controller
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CombatEquipmentTypeService implements GraphQLService {

  private static final List<String> availableSortFields = List.of("name", "category.name");

  private final CombatEquipmentTypeRepository combatEquipmentTypeRepository;
  private final CombatEquipmentCategoryRepository combatEquipmentCategoryRepository;
  private final CombatEquipmentAttributeRepository combatEquipmentAttributeRepository;
  private final CombatEquipmentTypeMapper combatEquipmentTypeMapper;
  private final AttributeMapper attributeMapper;

  @Cacheable(
      value = "combatEquipmentTypes",
      key = "#a0 + '_' + #a1 + '_' + (#a2 != null ? #a2.toString() : 'null')",
      unless = "#result.size() > 1000")
  public List<CombatEquipmentType> getAll(
      CombatEquipmentTypeFilter filter, Pagination pagination, List<Sorting> sorts) {
    log.info(
        "Get combat equipment types: filter={}, pagination={}, sorts={}",
        filter,
        pagination,
        sorts);
    var sort = generateSort(sorts, availableSortFields);
    var pageable = generatePageable(pagination, sort);
    var spec = generateCombatEquipmentTypeSpec(filter);
    return combatEquipmentTypeRepository.findAll(spec, pageable, sort);
  }

  @Cacheable(value = "combatEquipmentTypeCount", key = "(#a0 != null ? #a0 : 'null')", sync = true)
  public long getAllCount(CombatEquipmentTypeFilter filter) {
    log.info("Get combat equipment types count: filter={}", filter);
    var spec = generateCombatEquipmentTypeSpec(filter);
    return combatEquipmentTypeRepository.count(spec);
  }

  @Cacheable(value = "combatEquipmentTypeByNameAndCategory", key = "#a0 + '_' + #a1", sync = true)
  public CombatEquipmentType getByNameAndCategory(@NonNull String name, @NonNull String category) {
    log.info("Get combat equipment type: name={}, category={}", name, category);
    return combatEquipmentTypeRepository.findByNameAndCategory_Name(name, category).orElse(null);
  }

  @Transactional
  @Caching(
      put =
          @CachePut(
              value = "combatEquipmentTypeByNameAndCategory",
              key = "#a0.name + '_' + #a0.category"),
      evict = {
        @CacheEvict(value = "combatEquipmentTypes", allEntries = true),
        @CacheEvict(value = "combatEquipmentTypeCount", allEntries = true)
      })
  public CombatEquipmentType create(@Valid @NonNull CombatEquipmentTypeInput input) {
    log.info("Create combat equipment type: input={}", input);
    if (combatEquipmentTypeRepository.existsByNameAndCategory_Name(
        input.getName(), input.getCategory())) {
      throw new CombatEquipmentTypeAlreadyExistsException();
    }

    var type = combatEquipmentTypeMapper.toEntity(input);
    type.setCategory(
        combatEquipmentCategoryRepository
            .findByName(input.getCategory())
            .orElseThrow(CombatEquipmentCategoryNotFoundException::new));

    var attributes = toEntities(input.getAttributes(), type);
    type.setAttributes(attributes);

    return combatEquipmentTypeRepository.save(type);
  }

  @Transactional
  @Caching(
      put = @CachePut(value = "combatEquipmentTypeByNameAndCategory", key = "#a0 + '_' + #a1"),
      evict = {
        @CacheEvict(value = "combatEquipmentTypes", allEntries = true),
        @CacheEvict(value = "combatEquipmentTypeCount", allEntries = true)
      })
  public CombatEquipmentType update(
      @NonNull String name,
      @NonNull String category,
      @Valid @NonNull CombatEquipmentTypeInput input) {
    log.info("Update combat equipment type: name={}, category={}, input={}", name, category, input);
    var type =
        combatEquipmentTypeRepository
            .findByNameAndCategory_Name(name, category)
            .orElseThrow(CombatEquipmentTypeNotFoundException::new);

    combatEquipmentTypeMapper.partialUpdate(input, type);
    type.setCategory(
        combatEquipmentCategoryRepository
            .findByName(input.getCategory())
            .orElseThrow(CombatEquipmentCategoryNotFoundException::new));

    combatEquipmentAttributeRepository.deleteAllInBatch(type.getAttributes());
    var attributes = toEntities(input.getAttributes(), type);
    type.setAttributes(attributes);

    return combatEquipmentTypeRepository.save(type);
  }

  @Transactional
  @Caching(
      evict = {
        @CacheEvict(value = "combatEquipmentTypes", allEntries = true),
        @CacheEvict(value = "combatEquipmentTypeCount", allEntries = true),
        @CacheEvict(value = "combatEquipmentTypeByNameAndCategory", key = "#a0 + '_' + #a1")
      })
  public long delete(@NonNull String name, @NonNull String category) {
    log.info("Delete combat equipment type: name={}, category={}", name, category);
    return combatEquipmentTypeRepository.deleteByNameAndCategory_Name(name, category);
  }

  @Override
  @Cacheable(value = "reference", key = "#a0", sync = true)
  public CombatEquipmentType resolveReference(@NonNull Map<String, Object> reference) {
    log.info("Resolve combat equipment reference: reference={}", reference);
    if (reference.get("name") instanceof String name
        && reference.get("category") instanceof String category) {
      return combatEquipmentTypeRepository.findByNameAndCategory_Name(name, category).orElse(null);
    }
    return null;
  }

  private Set<CombatEquipmentAttribute> toEntities(
      Set<AttributeInput> attributeInputs, CombatEquipmentType type) {
    return attributeInputs.stream()
        .map(attributeDto -> toEntity(attributeDto, type))
        .collect(Collectors.toSet());
  }

  private CombatEquipmentAttribute toEntity(
      AttributeInput attributeInput, CombatEquipmentType type) {
    var attribute = attributeMapper.toEntity(attributeInput);
    attribute.setType(type);
    return attribute;
  }
}
