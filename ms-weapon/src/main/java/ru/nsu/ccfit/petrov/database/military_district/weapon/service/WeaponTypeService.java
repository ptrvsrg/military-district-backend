package ru.nsu.ccfit.petrov.database.military_district.weapon.service;

import static ru.nsu.ccfit.petrov.database.military_district.weapon.util.SpecPageSortUtils.generatePageable;
import static ru.nsu.ccfit.petrov.database.military_district.weapon.util.SpecPageSortUtils.generateSort;
import static ru.nsu.ccfit.petrov.database.military_district.weapon.util.SpecPageSortUtils.generateWeaponTypeSpec;

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
import ru.nsu.ccfit.petrov.database.military_district.weapon.dto.AttributeInput;
import ru.nsu.ccfit.petrov.database.military_district.weapon.dto.Pagination;
import ru.nsu.ccfit.petrov.database.military_district.weapon.dto.Sorting;
import ru.nsu.ccfit.petrov.database.military_district.weapon.dto.WeaponTypeFilter;
import ru.nsu.ccfit.petrov.database.military_district.weapon.dto.WeaponTypeInput;
import ru.nsu.ccfit.petrov.database.military_district.weapon.exception.WeaponCategoryNotFoundException;
import ru.nsu.ccfit.petrov.database.military_district.weapon.exception.WeaponTypeAlreadyExistsException;
import ru.nsu.ccfit.petrov.database.military_district.weapon.exception.WeaponTypeNotFoundException;
import ru.nsu.ccfit.petrov.database.military_district.weapon.mapper.AttributeMapper;
import ru.nsu.ccfit.petrov.database.military_district.weapon.mapper.WeaponTypeMapper;
import ru.nsu.ccfit.petrov.database.military_district.weapon.persistence.entity.WeaponAttribute;
import ru.nsu.ccfit.petrov.database.military_district.weapon.persistence.entity.WeaponType;
import ru.nsu.ccfit.petrov.database.military_district.weapon.persistence.repository.WeaponAttributeRepository;
import ru.nsu.ccfit.petrov.database.military_district.weapon.persistence.repository.WeaponCategoryRepository;
import ru.nsu.ccfit.petrov.database.military_district.weapon.persistence.repository.WeaponTypeRepository;

@Controller
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class WeaponTypeService implements GraphQLService {

  private static final List<String> availableSortFields = List.of("name", "category.name");

  private final WeaponTypeRepository weaponTypeRepository;
  private final WeaponCategoryRepository weaponCategoryRepository;
  private final WeaponAttributeRepository weaponAttributeRepository;
  private final WeaponTypeMapper weaponTypeMapper;
  private final AttributeMapper attributeMapper;

  @Cacheable(
      value = "weaponTypes",
      key = "#a0 + '_' + #a1 + '_' + (#a2 != null ? #a2.toString() : 'null')",
      sync = true)
  public List<WeaponType> getAll(
      WeaponTypeFilter filter, Pagination pagination, List<Sorting> sorts) {
    log.info(
        "Get combat weapon types: filter={}, pagination={}, sorts={}", filter, pagination, sorts);
    var sort = generateSort(sorts, availableSortFields);
    var pageable = generatePageable(pagination, sort);
    var spec = generateWeaponTypeSpec(filter);
    return weaponTypeRepository.findAll(spec, pageable, sort);
  }

  @Cacheable(value = "weaponTypeCount", key = "(#a0 != null ? #a0 : 'null')", sync = true)
  public long getAllCount(WeaponTypeFilter filter) {
    log.info("Get combat weapon types count: filter={}", filter);
    var spec = generateWeaponTypeSpec(filter);
    return weaponTypeRepository.count(spec);
  }

  @Cacheable(value = "weaponTypeByNameAndCategory", key = "#a0 + '_' + #a1", sync = true)
  public WeaponType getByNameAndCategory(@NonNull String name, @NonNull String category) {
    log.info("Get combat weapon type: name={}, category={}", name, category);
    return weaponTypeRepository.findByNameAndCategory_Name(name, category).orElse(null);
  }

  @Transactional
  @Caching(
      put = @CachePut(value = "weaponTypeByNameAndCategory", key = "#a0.name + '_' + #a0.category"),
      evict = {
        @CacheEvict(value = "weaponTypes", allEntries = true),
        @CacheEvict(value = "weaponTypeCount", allEntries = true)
      })
  public WeaponType create(@Valid @NonNull WeaponTypeInput input) {
    log.info("Create combat weapon type: input={}", input);
    if (weaponTypeRepository.existsByNameAndCategory_Name(input.getName(), input.getCategory())) {
      throw new WeaponTypeAlreadyExistsException();
    }

    var type = weaponTypeMapper.toEntity(input);
    type.setCategory(
        weaponCategoryRepository
            .findByName(input.getCategory())
            .orElseThrow(WeaponCategoryNotFoundException::new));

    var attributes = toEntities(input.getAttributes(), type);
    type.setAttributes(attributes);

    return weaponTypeRepository.save(type);
  }

  @Transactional
  @Caching(
      put = @CachePut(value = "weaponTypeByNameAndCategory", key = "#a0 + '_' + #a1"),
      evict = {
        @CacheEvict(value = "weaponTypes", allEntries = true),
        @CacheEvict(value = "weaponTypeCount", allEntries = true)
      })
  public WeaponType update(
      @NonNull String name, @NonNull String category, @Valid @NonNull WeaponTypeInput input) {
    log.info("Update combat weapon type: name={}, category={}, input={}", name, category, input);
    var type =
        weaponTypeRepository
            .findByNameAndCategory_Name(name, category)
            .orElseThrow(WeaponTypeNotFoundException::new);

    weaponTypeMapper.partialUpdate(input, type);
    type.setCategory(
        weaponCategoryRepository
            .findByName(input.getCategory())
            .orElseThrow(WeaponCategoryNotFoundException::new));

    weaponAttributeRepository.deleteAllInBatch(type.getAttributes());
    var attributes = toEntities(input.getAttributes(), type);
    type.setAttributes(attributes);

    return weaponTypeRepository.save(type);
  }

  @Transactional
  @Caching(
      evict = {
        @CacheEvict(value = "weaponTypes", allEntries = true),
        @CacheEvict(value = "weaponTypeCount", allEntries = true),
        @CacheEvict(value = "weaponTypeByNameAndCategory", key = "#a0 + '_' + #a1")
      })
  public long delete(@NonNull String name, @NonNull String category) {
    log.info("Delete combat weapon type: name={}, category={}", name, category);
    return weaponTypeRepository.deleteByNameAndCategory_Name(name, category);
  }

  @Override
  @Cacheable(value = "reference", key = "#a0", sync = true)
  public WeaponType resolveReference(@NonNull Map<String, Object> reference) {
    log.info("Resolve combat weapon reference: reference={}", reference);
    if (reference.get("name") instanceof String name
        && reference.get("category") instanceof String category) {
      return weaponTypeRepository.findByNameAndCategory_Name(name, category).orElse(null);
    }
    return null;
  }

  private Set<WeaponAttribute> toEntities(Set<AttributeInput> attributeInputs, WeaponType type) {
    return attributeInputs.stream()
        .map(attributeDto -> toEntity(attributeDto, type))
        .collect(Collectors.toSet());
  }

  private WeaponAttribute toEntity(AttributeInput attributeInput, WeaponType type) {
    var attribute = attributeMapper.toEntity(attributeInput);
    attribute.setType(type);
    return attribute;
  }
}
