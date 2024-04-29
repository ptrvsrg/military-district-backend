package ru.nsu.ccfit.petrov.database.military_district.infrastructure.service;

import static ru.nsu.ccfit.petrov.database.military_district.infrastructure.util.SpecPageSortUtils.generateBuildingSpec;
import static ru.nsu.ccfit.petrov.database.military_district.infrastructure.util.SpecPageSortUtils.generatePageable;
import static ru.nsu.ccfit.petrov.database.military_district.infrastructure.util.SpecPageSortUtils.generateSort;

import jakarta.validation.Valid;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.ccfit.petrov.database.military_district.infrastructure.dto.BuildingFilter;
import ru.nsu.ccfit.petrov.database.military_district.infrastructure.dto.BuildingInput;
import ru.nsu.ccfit.petrov.database.military_district.infrastructure.dto.Pagination;
import ru.nsu.ccfit.petrov.database.military_district.infrastructure.dto.Sorting;
import ru.nsu.ccfit.petrov.database.military_district.infrastructure.exception.BuildingAlreadyExistsException;
import ru.nsu.ccfit.petrov.database.military_district.infrastructure.exception.BuildingNotFoundException;
import ru.nsu.ccfit.petrov.database.military_district.infrastructure.mapper.AttributeMapper;
import ru.nsu.ccfit.petrov.database.military_district.infrastructure.mapper.BuildingMapper;
import ru.nsu.ccfit.petrov.database.military_district.infrastructure.persistence.entity.Building;
import ru.nsu.ccfit.petrov.database.military_district.infrastructure.persistence.repository.AttributeRepository;
import ru.nsu.ccfit.petrov.database.military_district.infrastructure.persistence.repository.BuildingRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class BuildingService implements GraphQLService {

  private static final List<String> availableSortFields = List.of("name", "address", "unit.name");

  private final AttributeRepository attributeRepository;
  private final BuildingRepository buildingRepository;
  private final AttributeMapper attributeMapper;
  private final BuildingMapper buildingMapper;

  @Cacheable("buildings")
  public List<Building> getAll(BuildingFilter filter, Pagination pagination, List<Sorting> sorts) {
    log.info("Get all buildings: filter={}, pagination={}, sorts={}", filter, pagination, sorts);
    var sort = generateSort(sorts, availableSortFields);
    var pageable = generatePageable(pagination, sort);
    var spec = generateBuildingSpec(filter);
    return buildingRepository.findAll(spec, pageable, sort);
  }

  @Cacheable("buildingCount")
  public long getAllCount(BuildingFilter filter) {
    log.info("Get all buildings count: filter={}", filter);
    var spec = generateBuildingSpec(filter);
    return buildingRepository.count(spec);
  }

  @Cacheable("buildingByNameAndUnit")
  public Building getByNameAndUnit(@NonNull String name, String unit) {
    log.info("Get building: name={}, unit={}", name, unit);
    return buildingRepository.findByNameAndUnit_Name(name, unit).orElse(null);
  }

  @Transactional
  public Building create(@Valid @NonNull BuildingInput buildingInput) {
    log.info("Create building: input={}", buildingInput);
    if (buildingRepository.existsByNameAndUnit_Name(
        buildingInput.getName(), buildingInput.getUnit())) {
      throw new BuildingAlreadyExistsException();
    }

    var building = buildingMapper.toEntity(buildingInput);
    return buildingRepository.save(building);
  }

  @Transactional
  public Building update(
      @NonNull String name, String unit, @Valid @NonNull BuildingInput buildingInput) {
    log.info("Update building: name={}, unit={}, input={}", name, unit, buildingInput);
    var building =
        buildingRepository
            .findByNameAndUnit_Name(name, unit)
            .orElseThrow(BuildingNotFoundException::new);
    if ((!name.equals(buildingInput.getName())
            || unit != null && !unit.equals(buildingInput.getUnit()))
        && buildingRepository.existsByNameAndUnit_Name(
            buildingInput.getName(), buildingInput.getUnit())) {
      throw new BuildingAlreadyExistsException();
    }

    attributeRepository.deleteAllInBatch(building.getAttributes());
    var attributes = attributeMapper.toEntities(buildingInput.getAttributes());
    attributes.forEach(attribute -> attribute.setBuilding(building));
    attributeRepository.saveAll(attributes);

    buildingMapper.partialUpdate(buildingInput, building);
    building.setAttributes(new LinkedHashSet<>(attributes));

    return buildingRepository.save(building);
  }

  @Transactional
  public long delete(@NonNull String name, String unit) {
    log.info("Delete building: name={}, unit={}", name, unit);
    return buildingRepository.deleteByNameAndUnit_Name(name, unit);
  }

  @Override
  public Object resolveReference(@NonNull Map<String, Object> reference) {
    log.info("Resolve reference: reference={}", reference);
    if (reference.get("name") instanceof String name
        && reference.get("unit") instanceof String unit) {
      return getByNameAndUnit(name, unit);
    }
    return null;
  }
}
