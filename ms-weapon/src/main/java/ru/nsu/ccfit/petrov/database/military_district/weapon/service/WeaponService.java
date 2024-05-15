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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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

  @Cacheable(
      value = "weapons",
      key = "#a0 + '_' + #a1 + '_' + (#a2 != null ? #a2.toString() : 'null')",
      unless = "#result.size() > 1000",
      sync = true)
  public List<Weapon> getAll(WeaponFilter filter, Pagination pagination, List<Sorting> sorts) {
    log.info(
        "Get all combat weapons: filter={}, pagination={}, sorts={}", filter, pagination, sorts);
    var sort = generateSort(sorts, availableSortFields);
    var pageable = generatePageable(pagination, sort);
    var spec = generateWeaponSpec(filter);
    return weaponRepository.findAll(spec, pageable, sort);
  }

  @Cacheable(value = "weaponCount", key = "(#a0 != null ? #a0 : 'null')", sync = true)
  public long getAllCount(WeaponFilter filter) {
    log.info("Get all combat weapons count: filter={}", filter);
    var spec = generateWeaponSpec(filter);
    return weaponRepository.count(spec);
  }

  @Cacheable(value = "weaponBySerialNumber", key = "#a0", sync = true)
  public Weapon getBySerialNumber(@NonNull String serialNumber) {
    log.info("Get combat weapon by serial number: serialNumber={}", serialNumber);
    return weaponRepository.findBySerialNumber(serialNumber).orElse(null);
  }

  @Transactional
  @Caching(
      put = @CachePut(value = "weaponBySerialNumber", key = "#a0.serialNumber"),
      evict = {
        @CacheEvict(value = "weapons", allEntries = true),
        @CacheEvict(value = "weaponCount", allEntries = true)
      })
  public Weapon create(@Valid @NonNull WeaponInput weaponInput) {
    log.info("Create combat weapon: input={}", weaponInput);
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
  @Caching(
      put = @CachePut(value = "weaponBySerialNumber", key = "#a0"),
      evict = {
        @CacheEvict(value = "weapons", allEntries = true),
        @CacheEvict(value = "weaponCount", allEntries = true)
      })
  public Weapon update(@NonNull String serialNumber, @Valid @NonNull WeaponInput weaponInput) {
    log.info("Update combat weapon: serialNumber={}, input={}", serialNumber, weaponInput);
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
  @Caching(
      evict = {
        @CacheEvict(value = "weapons", allEntries = true),
        @CacheEvict(value = "weaponCount", allEntries = true),
        @CacheEvict(value = "weaponBySerialNumber", key = "#a0")
      })
  public long delete(@NonNull String serialNumber) {
    log.info("Delete combat weapon: serialNumber={}", serialNumber);
    return weaponRepository.deleteBySerialNumber(serialNumber);
  }

  @Override
  @Cacheable(value = "reference", key = "#a0", sync = true)
  public Weapon resolveReference(@NonNull Map<String, Object> reference) {
    log.info("Resolve combat weapon reference: reference={}", reference);
    if (reference.get("serialNumber") instanceof String serialNumber) {
      return weaponRepository.findBySerialNumber(serialNumber).orElse(null);
    }
    return null;
  }
}
