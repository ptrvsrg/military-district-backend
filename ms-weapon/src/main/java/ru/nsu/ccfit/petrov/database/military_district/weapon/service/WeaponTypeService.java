package ru.nsu.ccfit.petrov.database.military_district.weapon.service;

import static ru.nsu.ccfit.petrov.database.military_district.weapon.util.SpecPageSortUtils.generatePageable;
import static ru.nsu.ccfit.petrov.database.military_district.weapon.util.SpecPageSortUtils.generateSort;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.ccfit.petrov.database.military_district.weapon.dto.AttributeDto;
import ru.nsu.ccfit.petrov.database.military_district.weapon.dto.WeaponTypeDto;
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
public class WeaponTypeService implements GraphQLService {

  private static final List<String> availableSortFields = List.of("name", "category.name");

  private final WeaponTypeRepository weaponTypeRepository;
  private final WeaponCategoryRepository weaponCategoryRepository;
  private final WeaponAttributeRepository weaponAttributeRepository;
  private final WeaponTypeMapper weaponTypeMapper;
  private final AttributeMapper attributeMapper;

  public List<WeaponType> getAll(
      String name,
      String category,
      Integer page,
      Integer pageSize,
      String sortField,
      Boolean sortAsc) {
    var sort = generateSort(sortField, sortAsc, availableSortFields);
    var pageable = generatePageable(page, pageSize, sort);
    var spec = generateSpecification(name, category);
    return weaponTypeRepository.findAll(spec, pageable, sort);
  }

  public long getAllCount(String name, String category) {
    var spec = generateSpecification(name, category);
    if (spec == null) {
      return weaponTypeRepository.count();
    }
    return weaponTypeRepository.count(spec);
  }

  public WeaponType getByNameAndCategory(@NonNull String name, @NonNull String category) {
    return weaponTypeRepository.findByNameAndCategory_Name(name, category).orElse(null);
  }

  @Transactional
  public WeaponType create(@Valid @NonNull WeaponTypeDto typeDto) {
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
      @NonNull String name, @NonNull String category, @Valid @NonNull WeaponTypeDto typeDto) {
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
    return weaponTypeRepository.deleteByNameAndCategory_Name(name, category);
  }

  private Specification<WeaponType> generateSpecification(String name, String category) {
    Specification<WeaponType> spec = null;
    if (Objects.nonNull(name)) {
      spec = (root, query, builder) -> builder.like(root.get("name"), "%" + name + "%");
    }
    if (Objects.nonNull(category)) {
      Specification<WeaponType> newSpec =
          (root, query, builder) ->
              builder.like(root.get("category").get("name"), "%" + category + "%");
      spec = Objects.isNull(spec) ? newSpec : spec.and(newSpec);
    }
    return spec;
  }

  private Set<WeaponAttribute> toEntities(Set<AttributeDto> attributeDtos, WeaponType type) {
    return attributeDtos.stream()
        .map(attributeDto -> toEntity(attributeDto, type))
        .collect(Collectors.toSet());
  }

  private WeaponAttribute toEntity(AttributeDto attributeDto, WeaponType type) {
    var attribute = attributeMapper.toEntity(attributeDto);
    attribute.setType(type);
    return attribute;
  }

  @Override
  public Object resolveReference(@NonNull Map<String, Object> reference) {
    if (reference.get("name") instanceof String name
        && reference.get("category") instanceof String category) {
      return getByNameAndCategory(name, category);
    }
    return null;
  }
}
