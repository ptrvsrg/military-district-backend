package ru.nsu.ccfit.petrov.database.military_district.weapon.service;

import static ru.nsu.ccfit.petrov.database.military_district.weapon.util.SpecPageSortUtils.generatePageable;
import static ru.nsu.ccfit.petrov.database.military_district.weapon.util.SpecPageSortUtils.generateSort;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.ccfit.petrov.database.military_district.weapon.dto.WeaponDto;
import ru.nsu.ccfit.petrov.database.military_district.weapon.exception.WeaponAlreadyExistsException;
import ru.nsu.ccfit.petrov.database.military_district.weapon.exception.WeaponNotFoundException;
import ru.nsu.ccfit.petrov.database.military_district.weapon.exception.WeaponTypeNotFoundException;
import ru.nsu.ccfit.petrov.database.military_district.weapon.mapper.WeaponMapper;
import ru.nsu.ccfit.petrov.database.military_district.weapon.persistence.entity.Weapon;
import ru.nsu.ccfit.petrov.database.military_district.weapon.persistence.repository.WeaponRepository;
import ru.nsu.ccfit.petrov.database.military_district.weapon.persistence.repository.WeaponTypeRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WeaponService implements GraphQLService {

  private static final List<String> availableSortFields =
      List.of("serialNumber", "type.name", "type.category.name", "unit.name");

  private final WeaponRepository weaponRepository;
  private final WeaponTypeRepository weaponTypeRepository;
  private final WeaponMapper weaponMapper;

  public List<Weapon> getAll(
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
    return weaponRepository.findAll(spec, pageable, sort);
  }

  public long getAllCount(String type, String category, String unit) {
    var spec = generateSpecification(type, category, unit);
    if (spec == null) {
      return weaponRepository.count();
    }
    return weaponRepository.count(spec);
  }

  public Weapon getBySerialNumber(@NonNull String serialNumber) {
    return weaponRepository.findBySerialNumber(serialNumber).orElse(null);
  }

  @Transactional
  public Weapon create(@Valid @NonNull WeaponDto weaponDto) {
    if (weaponRepository.existsBySerialNumber(weaponDto.getSerialNumber())) {
      throw new WeaponAlreadyExistsException();
    }

    var weapon = weaponMapper.toEntity(weaponDto);
    if (weaponDto.getType() != null) {
      weapon.setType(
          weaponTypeRepository
              .findByName(weaponDto.getType())
              .orElseThrow(WeaponTypeNotFoundException::new));
    }

    return weaponRepository.save(weapon);
  }

  @Transactional
  public Weapon update(@NonNull String serialNumber, @Valid @NonNull WeaponDto weaponDto) {
    var weapon =
        weaponRepository.findBySerialNumber(serialNumber).orElseThrow(WeaponNotFoundException::new);

    weaponMapper.partialUpdate(weaponDto, weapon);
    if (weaponDto.getType() != null) {
      weapon.setType(
          weaponTypeRepository
              .findByName(weaponDto.getType())
              .orElseThrow(WeaponTypeNotFoundException::new));
    }

    return weaponRepository.save(weapon);
  }

  @Transactional
  public long delete(@NonNull String serialNumber) {
    return weaponRepository.deleteBySerialNumber(serialNumber);
  }

  private Specification<Weapon> generateSpecification(String type, String category, String unit) {
    Specification<Weapon> spec = null;
    if (Objects.nonNull(type)) {
      spec = (root, query, builder) -> builder.like(root.get("type").get("name"), "%" + type + "%");
    }
    if (Objects.nonNull(category)) {
      Specification<Weapon> newSpec =
          (root, query, builder) ->
              builder.like(root.get("type").get("category").get("name"), "%" + category + "%");
      spec = Objects.isNull(spec) ? newSpec : spec.and(newSpec);
    }
    if (Objects.nonNull(unit)) {
      Specification<Weapon> newSpec =
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
