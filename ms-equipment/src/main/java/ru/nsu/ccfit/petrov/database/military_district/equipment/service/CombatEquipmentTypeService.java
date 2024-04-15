package ru.nsu.ccfit.petrov.database.military_district.equipment.service;

import static ru.nsu.ccfit.petrov.database.military_district.equipment.util.SpecPageSortUtils.generatePageable;
import static ru.nsu.ccfit.petrov.database.military_district.equipment.util.SpecPageSortUtils.generateSort;

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
import ru.nsu.ccfit.petrov.database.military_district.equipment.dto.AttributeDto;
import ru.nsu.ccfit.petrov.database.military_district.equipment.dto.CombatEquipmentTypeDto;
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
public class CombatEquipmentTypeService implements GraphQLService {

  private static final List<String> availableSortFields = List.of("name", "category.name");

  private final CombatEquipmentTypeRepository combatEquipmentTypeRepository;
  private final CombatEquipmentCategoryRepository combatEquipmentCategoryRepository;
  private final CombatEquipmentAttributeRepository combatEquipmentAttributeRepository;
  private final CombatEquipmentTypeMapper combatEquipmentTypeMapper;
  private final AttributeMapper attributeMapper;

  public List<CombatEquipmentType> getAll(
      String name,
      String category,
      Integer page,
      Integer pageSize,
      String sortField,
      Boolean sortAsc) {
    var sort = generateSort(sortField, sortAsc, availableSortFields);
    var pageable = generatePageable(page, pageSize, sort);
    var spec = generateSpecification(name, category);
    return combatEquipmentTypeRepository.findAll(spec, pageable, sort);
  }

  public long getAllCount(String name, String category) {
    var spec = generateSpecification(name, category);
    if (spec == null) {
      return combatEquipmentTypeRepository.count();
    }
    return combatEquipmentTypeRepository.count(spec);
  }

  public CombatEquipmentType getByNameAndCategory(@NonNull String name, @NonNull String category) {
    return combatEquipmentTypeRepository.findByNameAndCategory_Name(name, category).orElse(null);
  }

  @Transactional
  public CombatEquipmentType create(@Valid @NonNull CombatEquipmentTypeDto typeDto) {
    if (combatEquipmentTypeRepository.existsByNameAndCategory_Name(
        typeDto.getName(), typeDto.getCategory())) {
      throw new CombatEquipmentTypeAlreadyExistsException();
    }

    var type = combatEquipmentTypeMapper.toEntity(typeDto);
    type.setCategory(
        combatEquipmentCategoryRepository
            .findByName(typeDto.getCategory())
            .orElseThrow(CombatEquipmentCategoryNotFoundException::new));

    var attributes = toEntities(typeDto.getAttributes(), type);
    type.setAttributes(attributes);

    return combatEquipmentTypeRepository.save(type);
  }

  @Transactional
  public CombatEquipmentType update(
      @NonNull String name,
      @NonNull String category,
      @Valid @NonNull CombatEquipmentTypeDto typeDto) {
    var type =
        combatEquipmentTypeRepository
            .findByNameAndCategory_Name(name, category)
            .orElseThrow(CombatEquipmentTypeNotFoundException::new);

    combatEquipmentTypeMapper.partialUpdate(typeDto, type);
    type.setCategory(
        combatEquipmentCategoryRepository
            .findByName(typeDto.getCategory())
            .orElseThrow(CombatEquipmentCategoryNotFoundException::new));

    combatEquipmentAttributeRepository.deleteAllInBatch(type.getAttributes());
    var attributes = toEntities(typeDto.getAttributes(), type);
    type.setAttributes(attributes);

    return combatEquipmentTypeRepository.save(type);
  }

  @Transactional
  public long delete(@NonNull String name, @NonNull String category) {
    return combatEquipmentTypeRepository.deleteByNameAndCategory_Name(name, category);
  }

  private Specification<CombatEquipmentType> generateSpecification(String name, String category) {
    Specification<CombatEquipmentType> spec = null;
    if (Objects.nonNull(name)) {
      spec = (root, query, builder) -> builder.like(root.get("name"), "%" + name + "%");
    }
    if (Objects.nonNull(category)) {
      Specification<CombatEquipmentType> newSpec =
          (root, query, builder) ->
              builder.like(root.get("category").get("name"), "%" + category + "%");
      spec = Objects.isNull(spec) ? newSpec : spec.and(newSpec);
    }
    return spec;
  }

  private Set<CombatEquipmentAttribute> toEntities(
      Set<AttributeDto> attributeDtos, CombatEquipmentType type) {
    return attributeDtos.stream()
        .map(attributeDto -> toEntity(attributeDto, type))
        .collect(Collectors.toSet());
  }

  private CombatEquipmentAttribute toEntity(AttributeDto attributeDto, CombatEquipmentType type) {
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
