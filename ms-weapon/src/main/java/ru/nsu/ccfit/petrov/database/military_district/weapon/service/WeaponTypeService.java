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

  public List<WeaponType> getAll(
      WeaponTypeFilter filter, Pagination pagination, List<Sorting> sorts) {
    log.info("Get all weapon types: filter={}, pagination={}, sorts={}", filter, pagination, sorts);
    var sort = generateSort(sorts, availableSortFields);
    var pageable = generatePageable(pagination, sort);
    var spec = generateWeaponTypeSpec(filter);
    return weaponTypeRepository.findAll(spec, pageable, sort);
  }

  public long getAllCount(WeaponTypeFilter filter) {
    log.info("Get all weapon types count: filter={}", filter);
    var spec = generateWeaponTypeSpec(filter);
    return weaponTypeRepository.count(spec);
  }

  public WeaponType getByNameAndCategory(@NonNull String name, @NonNull String category) {
    log.info("Get weapon type: name={}, category={}", name, category);
    return weaponTypeRepository.findByNameAndCategory_Name(name, category).orElse(null);
  }

  @Transactional
  public WeaponType create(@Valid @NonNull WeaponTypeInput typeDto) {
    log.info("Create weapon type: input={}", typeDto);
    if (weaponTypeRepository.existsByNameAndCategory_Name(
        typeDto.getName(), typeDto.getCategory())) {
      throw new WeaponTypeAlreadyExistsException();
    }

    var type = weaponTypeMapper.toEntity(typeDto);
    type.setCategory(
        weaponCategoryRepository
            .findByName(typeDto.getCategory())
            .orElseThrow(WeaponCategoryNotFoundException::new));

    var attributes = toEntities(typeDto.getAttributes(), type);
    type.setAttributes(attributes);

    return weaponTypeRepository.save(type);
  }

  @Transactional
  public WeaponType update(
      @NonNull String name, @NonNull String category, @Valid @NonNull WeaponTypeInput typeDto) {
    log.info("Update weapon type: name={}, category={}, input={}", name, category, typeDto);
    var type =
        weaponTypeRepository
            .findByNameAndCategory_Name(name, category)
            .orElseThrow(WeaponTypeNotFoundException::new);

    weaponTypeMapper.partialUpdate(typeDto, type);
    type.setCategory(
        weaponCategoryRepository
            .findByName(typeDto.getCategory())
            .orElseThrow(WeaponCategoryNotFoundException::new));

    weaponAttributeRepository.deleteAllInBatch(type.getAttributes());
    var attributes = toEntities(typeDto.getAttributes(), type);
    type.setAttributes(attributes);

    return weaponTypeRepository.save(type);
  }

  @Transactional
  public long delete(@NonNull String name, @NonNull String category) {
    log.info("Delete weapon type: name={}, category={}", name, category);
    return weaponTypeRepository.deleteByNameAndCategory_Name(name, category);
  }

  @Override
  public Object resolveReference(@NonNull Map<String, Object> reference) {
    log.info("Resolve reference: reference={}", reference);
    if (reference.get("name") instanceof String name
        && reference.get("category") instanceof String category) {
      return getByNameAndCategory(name, category);
    }
    return null;
  }

  private Set<WeaponAttribute> toEntities(Set<AttributeInput> attributeInputs, WeaponType type) {
    return attributeInputs.stream()
        .map(attributeInput -> toEntity(attributeInput, type))
        .collect(Collectors.toSet());
  }

  private WeaponAttribute toEntity(AttributeInput attributeInput, WeaponType type) {
    var attribute = attributeMapper.toEntity(attributeInput);
    attribute.setType(type);
    return attribute;
  }
}
