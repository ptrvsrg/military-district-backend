package ru.nsu.ccfit.petrov.database.military_district.infrastructure.service;

import jakarta.validation.Valid;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.ccfit.petrov.database.military_district.infrastructure.dto.BuildingDto;
import ru.nsu.ccfit.petrov.database.military_district.infrastructure.exception.BuildingAlreadyExistsException;
import ru.nsu.ccfit.petrov.database.military_district.infrastructure.exception.BuildingNotFoundException;
import ru.nsu.ccfit.petrov.database.military_district.infrastructure.mapper.AttributeMapper;
import ru.nsu.ccfit.petrov.database.military_district.infrastructure.mapper.BuildingMapper;
import ru.nsu.ccfit.petrov.database.military_district.infrastructure.persistence.entity.Building;
import ru.nsu.ccfit.petrov.database.military_district.infrastructure.persistence.repository.AttributeRepository;
import ru.nsu.ccfit.petrov.database.military_district.infrastructure.persistence.repository.BuildingRepository;
import ru.nsu.ccfit.petrov.database.military_district.infrastructure.util.SpecPageSortUtils;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BuildingService implements GraphQLService {

  private static final List<String> availableSortFields = List.of("name", "address", "unit.name");

  private final AttributeRepository attributeRepository;
  private final BuildingRepository buildingRepository;
  private final AttributeMapper attributeMapper;
  private final BuildingMapper buildingMapper;

  public List<Building> getAll(
      String name,
      String address,
      String unit,
      Integer page,
      Integer pageSize,
      String sortField,
      Boolean sortAsc) {
    var sort = SpecPageSortUtils.generateSort(sortField, sortAsc, availableSortFields);
    var pageable = SpecPageSortUtils.generatePageable(page, pageSize, sort);
    var spec = generateSpecification(name, address, unit);
    return buildingRepository.findAll(spec, pageable, sort);
  }

  public long getAllCount(String name, String address, String unit) {
    var spec = generateSpecification(name, address, unit);
    if (spec == null) {
      return buildingRepository.count();
    }
    return buildingRepository.count(spec);
  }

  public Building getByNameAndUnit(@NonNull String name, String unit) {
    return buildingRepository.findByNameAndUnit_Name(name, unit).orElse(null);
  }

  @Transactional
  public Building create(@Valid @NonNull BuildingDto buildingDto) {
    if (buildingRepository.existsByNameAndUnit_Name(buildingDto.getName(), buildingDto.getUnit())) {
      throw new BuildingAlreadyExistsException();
    }

    var building = buildingMapper.toEntity(buildingDto);
    return buildingRepository.save(building);
  }

  @Transactional
  public Building update(
      @NonNull String name, String unit, @Valid @NonNull BuildingDto buildingDto) {
    var building =
        buildingRepository
            .findByNameAndUnit_Name(name, unit)
            .orElseThrow(BuildingNotFoundException::new);
    if ((!name.equals(buildingDto.getName()) || unit != null && !unit.equals(buildingDto.getUnit()))
        && buildingRepository.existsByNameAndUnit_Name(
            buildingDto.getName(), buildingDto.getUnit())) {
      throw new BuildingAlreadyExistsException();
    }

    attributeRepository.deleteAllInBatch(building.getAttributes());
    var attributes = attributeMapper.toEntities(buildingDto.getAttributes());
    attributes.forEach(attribute -> attribute.setBuilding(building));
    attributeRepository.saveAll(attributes);

    buildingMapper.partialUpdate(buildingDto, building);
    building.setAttributes(new LinkedHashSet<>(attributes));

    return buildingRepository.save(building);
  }

  @Transactional
  public long delete(@NonNull String name, String unit) {
    return buildingRepository.deleteByNameAndUnit_Name(name, unit);
  }

  private Specification<Building> generateSpecification(String name, String address, String unit) {
    Specification<Building> spec = null;
    if (Objects.nonNull(name)) {
      spec = (root, query, builder) -> builder.like(root.get("name"), "%" + name + "%");
    }
    if (Objects.nonNull(unit)) {
      Specification<Building> newSpec =
          (root, query, builder) -> builder.like(root.get("unit").get("name"), "%" + unit + "%");
      spec = Objects.isNull(spec) ? newSpec : spec.and(newSpec);
    }
    if (Objects.nonNull(address)) {
      Specification<Building> newSpec =
          (root, query, builder) -> {
            var concatParts =
                List.of(
                    builder.concat(root.get("address").get("country"), ", "),
                    builder.concat(root.get("address").get("state"), ", "),
                    builder.concat(root.get("address").get("locality"), ", "),
                    builder.concat(root.get("address").get("street"), ", "),
                    builder.concat(root.get("address").get("houseNumber"), ", "),
                    builder.concat(root.get("address").get("postCode").as(String.class), ", "));
            var addressExpr = concatParts.stream().reduce(builder::concat).get();
            return builder.like(addressExpr, "%" + address + "%");
          };
      spec = Objects.isNull(spec) ? newSpec : spec.and(newSpec);
    }
    return spec;
  }

  @Override
  public Object resolveReference(@NonNull Map<String, Object> reference) {
    if (reference.get("name") instanceof String name
        && reference.get("unit") instanceof String unit) {
      return getByNameAndUnit(name, unit);
    }
    return null;
  }
}
