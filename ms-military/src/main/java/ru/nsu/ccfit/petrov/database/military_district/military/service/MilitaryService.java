package ru.nsu.ccfit.petrov.database.military_district.military.service;

import static ru.nsu.ccfit.petrov.database.military_district.military.util.SpecPageSortUtils.generateMilitarySpec;
import static ru.nsu.ccfit.petrov.database.military_district.military.util.SpecPageSortUtils.generatePageable;
import static ru.nsu.ccfit.petrov.database.military_district.military.util.SpecPageSortUtils.generateSort;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.ccfit.petrov.database.military_district.military.dto.AttributeInput;
import ru.nsu.ccfit.petrov.database.military_district.military.dto.MilitaryFilter;
import ru.nsu.ccfit.petrov.database.military_district.military.dto.MilitaryInput;
import ru.nsu.ccfit.petrov.database.military_district.military.dto.Pagination;
import ru.nsu.ccfit.petrov.database.military_district.military.dto.Sorting;
import ru.nsu.ccfit.petrov.database.military_district.military.exception.MilitaryAlreadyExistsException;
import ru.nsu.ccfit.petrov.database.military_district.military.exception.MilitaryNotFoundException;
import ru.nsu.ccfit.petrov.database.military_district.military.exception.RankNotFoundException;
import ru.nsu.ccfit.petrov.database.military_district.military.mapper.AttributeMapper;
import ru.nsu.ccfit.petrov.database.military_district.military.mapper.MilitaryMapper;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.entity.Attribute;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.entity.Military;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.repository.AttributeRepository;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.repository.MilitaryRepository;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.repository.RankRepository;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.repository.SpecialtyRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MilitaryService implements GraphQLService {

  private static final List<String> availableSortFields =
      List.of("mbn", "firstName", "lastName", "middleName", "birthDate", "rank.level", "unit.name");

  private final MilitaryRepository militaryRepository;
  private final RankRepository rankRepository;
  private final SpecialtyRepository specialtyRepository;
  private final AttributeRepository attributeRepository;
  private final MilitaryMapper militaryMapper;
  private final AttributeMapper attributeMapper;

  @Cacheable(value = "militaries", key = "#a0 + '_' + #a1 + '_' + #a2", sync = true)
  public List<Military> getAll(MilitaryFilter filter, Pagination pagination, List<Sorting> sorts) {
    log.info("Get all militaries: filter={}, pagination={}, sorts={}", filter, pagination, sorts);
    var sort = generateSort(sorts, availableSortFields);
    var pageable = generatePageable(pagination, sort);
    var spec = generateMilitarySpec(filter);
    return militaryRepository.findAll(spec, pageable, sort);
  }

  @Cacheable(value = "militaryCount", key = "'filter_' + #a0", sync = true)
  public long getAllCount(MilitaryFilter filter) {
    log.info("Get all military count: filter={}", filter);
    var spec = generateMilitarySpec(filter);
    return militaryRepository.count(spec);
  }

  @Cacheable(value = "militaryByMbn", key = "#a0", sync = true)
  public Military getByMbn(@NonNull String mbn) {
    log.info("Get military: mbn={}", mbn);
    return militaryRepository.findByMbn(mbn).orElse(null);
  }

  @Transactional
  @Caching(
      put = @CachePut(value = "militaryByMbn", key = "#a0.mbn"),
      evict = {
        @CacheEvict(value = "militaries", allEntries = true),
        @CacheEvict(value = "militaryCount", allEntries = true)
      })
  public Military create(@Valid @NonNull MilitaryInput militaryInput) {
    log.info("Create military: input={}", militaryInput);
    if (militaryRepository.existsByMbn(militaryInput.getMbn())) {
      throw new MilitaryAlreadyExistsException();
    }

    var military = militaryMapper.toEntity(militaryInput);
    if (militaryInput.getRank() != null) {
      military.setRank(
          rankRepository
              .findByName(militaryInput.getRank())
              .orElseThrow(RankNotFoundException::new));
    }
    military.setSpecialties(specialtyRepository.findAllByCodeIn(militaryInput.getSpecialties()));

    var attributes = toEntities(militaryInput.getAttributes());
    attributes.forEach(attribute -> attribute.setMilitary(military));
    military.setAttributes(attributes);

    return militaryRepository.save(military);
  }

  @Transactional
  @Caching(
      put = @CachePut(value = "militaryByMbn", key = "#a0"),
      evict = {
        @CacheEvict(value = "militaries", allEntries = true),
        @CacheEvict(value = "militaryCount", allEntries = true)
      })
  public Military update(@NonNull String mbn, @Valid @NonNull MilitaryInput militaryInput) {
    log.info("Update military: mbn={}, input={}", mbn, militaryInput);
    var military = militaryRepository.findByMbn(mbn).orElseThrow(MilitaryNotFoundException::new);

    militaryMapper.partialUpdate(militaryInput, military);
    if (militaryInput.getRank() != null) {
      military.setRank(
          rankRepository
              .findByName(militaryInput.getRank())
              .orElseThrow(RankNotFoundException::new));
    }
    military.setSpecialties(specialtyRepository.findAllByCodeIn(militaryInput.getSpecialties()));

    attributeRepository.deleteAllInBatch(military.getAttributes());
    var attributes = toEntities(militaryInput.getAttributes());
    attributes.forEach(attribute -> attribute.setMilitary(military));
    military.setAttributes(attributes);

    return militaryRepository.save(military);
  }

  @Transactional
  @Caching(
      evict = {
        @CacheEvict(value = "militaries", allEntries = true),
        @CacheEvict(value = "militaryCount", allEntries = true),
        @CacheEvict(value = "militaryByMbn", key = "#a0")
      })
  public long delete(@NonNull String mbn) {
    log.info("Delete military: mbn={}", mbn);
    return militaryRepository.deleteByMbn(mbn);
  }

  @Override
  @Cacheable(value = "reference", key = "#a0", sync = true)
  public Military resolveReference(@NonNull Map<String, Object> reference) {
    log.info("Resolve reference: reference={}", reference);
    if (reference.get("mbn") instanceof String mbn) {
      return militaryRepository.findByMbn(mbn).orElse(null);
    }
    return null;
  }

  private Set<Attribute> toEntities(Set<AttributeInput> attributeInputs) {
    return attributeInputs.stream().map(this::toEntity).collect(Collectors.toSet());
  }

  private Attribute toEntity(AttributeInput attributeInput) {
    var attribute = attributeMapper.toEntity(attributeInput);
    attribute.setRank(
        rankRepository
            .findByName(attributeInput.getRank())
            .orElseThrow(RankNotFoundException::new));
    return attribute;
  }
}
