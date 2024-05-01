package ru.nsu.ccfit.petrov.database.military_district.weapon.service;

import static ru.nsu.ccfit.petrov.database.military_district.weapon.util.SpecPageSortUtils.generatePageable;
import static ru.nsu.ccfit.petrov.database.military_district.weapon.util.SpecPageSortUtils.generateSort;
import static ru.nsu.ccfit.petrov.database.military_district.weapon.util.SpecPageSortUtils.generateWeaponSpec;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.ccfit.petrov.database.military_district.weapon.dto.Pagination;
import ru.nsu.ccfit.petrov.database.military_district.weapon.dto.Sorting;
import ru.nsu.ccfit.petrov.database.military_district.weapon.dto.WeaponFilter;
import ru.nsu.ccfit.petrov.database.military_district.weapon.dto.WeaponInput;
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
@Slf4j
public class WeaponService implements GraphQLService {

  private static final List<String> availableSortFields =
      List.of("serialNumber", "type.name", "type.category.name", "unit.name");

  private final WeaponRepository weaponRepository;
  private final WeaponTypeRepository weaponTypeRepository;
  private final WeaponMapper weaponMapper;

  @Cacheable("weapons")
  public List<Weapon> getAll(WeaponFilter filter, Pagination pagination, List<Sorting> sorts) {
    log.info("Get all weapons: filter={}, pagination={}, sorts={}", filter, pagination, sorts);
    var sort = generateSort(sorts, availableSortFields);
    var pageable = generatePageable(pagination, sort);
    var spec = generateWeaponSpec(filter);
    return weaponRepository.findAll(spec, pageable, sort);
  }

  @Cacheable("weaponCount")
  public long getAllCount(WeaponFilter filter) {
    log.info("Get all weapons count: filter={}", filter);
    var spec = generateWeaponSpec(filter);
    return weaponRepository.count(spec);
  }

  @Cacheable("weaponBySerialNumber")
  public Weapon getBySerialNumber(@NonNull String serialNumber) {
    log.info("Get weapon by serial number: serialNumber={}", serialNumber);
    return weaponRepository.findBySerialNumber(serialNumber).orElse(null);
  }

  @Transactional
  public Weapon create(@Valid @NonNull WeaponInput weaponInput) {
    log.info("Create weapon: input={}", weaponInput);
    if (weaponRepository.existsBySerialNumber(weaponInput.getSerialNumber())) {
      throw new WeaponAlreadyExistsException();
    }

    var weapon = weaponMapper.toEntity(weaponInput);
    if (weaponInput.getType() != null) {
      weapon.setType(
          weaponTypeRepository
              .findByName(weaponInput.getType())
              .orElseThrow(WeaponTypeNotFoundException::new));
    }

    return weaponRepository.save(weapon);
  }

  @Transactional
  public Weapon update(@NonNull String serialNumber, @Valid @NonNull WeaponInput weaponInput) {
    log.info("Update weapon: serialNumber={}, input={}", serialNumber, weaponInput);
    var weapon =
        weaponRepository.findBySerialNumber(serialNumber).orElseThrow(WeaponNotFoundException::new);

    weaponMapper.partialUpdate(weaponInput, weapon);
    if (weaponInput.getType() != null) {
      weapon.setType(
          weaponTypeRepository
              .findByName(weaponInput.getType())
              .orElseThrow(WeaponTypeNotFoundException::new));
    }

    return weaponRepository.save(weapon);
  }

  @Transactional
  public long delete(@NonNull String serialNumber) {
    log.info("Delete weapon: serialNumber={}", serialNumber);
    return weaponRepository.deleteBySerialNumber(serialNumber);
  }

  @Override
  public Object resolveReference(@NonNull Map<String, Object> reference) {
    log.info("Resolve reference: reference={}", reference);
    if (reference.get("serialNumber") instanceof String serialNumber) {
      return getBySerialNumber(serialNumber);
    }
    return null;
  }
}