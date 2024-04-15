package ru.nsu.ccfit.petrov.database.military_district.military.service;

import static ru.nsu.ccfit.petrov.database.military_district.military.util.PageSortUtils.generatePageable;
import static ru.nsu.ccfit.petrov.database.military_district.military.util.PageSortUtils.generateSort;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.ccfit.petrov.database.military_district.military.dto.AttributeDto;
import ru.nsu.ccfit.petrov.database.military_district.military.dto.MilitaryDto;
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
public class MilitaryService implements GraphQLService {

  private static final List<String> availableSortFields =
      List.of("mbn", "firstName", "lastName", "middleName", "birthDate", "rank.level", "unit.name");

  private final MilitaryRepository militaryRepository;
  private final RankRepository rankRepository;
  private final SpecialtyRepository specialtyRepository;
  private final AttributeRepository attributeRepository;
  private final MilitaryMapper militaryMapper;
  private final AttributeMapper attributeMapper;

  public List<Military> getAll(
      String firstName,
      String lastName,
      String middleName,
      String rank,
      String unit,
      Integer page,
      Integer pageSize,
      String sortField,
      Boolean sortAsc) {
    var sort = generateSort(sortField, sortAsc, availableSortFields);
    var pageable = generatePageable(page, pageSize, sort);
    var spec = generateSpecification(firstName, lastName, middleName, rank, unit);
    return militaryRepository.findAll(spec, pageable, sort);
  }

  public long getAllCount(
      String firstName, String lastName, String middleName, String rank, String unit) {
    var spec = generateSpecification(firstName, lastName, middleName, rank, unit);
    if (spec == null) {
      return militaryRepository.count();
    }
    return militaryRepository.count(spec);
  }

  public Military getByMbn(@NonNull String mbn) {
    return militaryRepository.findByMbn(mbn).orElse(null);
  }

  @Transactional
  public Military create(@Valid @NonNull MilitaryDto militaryDto) {
    if (militaryRepository.existsByMbn(militaryDto.getMbn())) {
      throw new MilitaryAlreadyExistsException();
    }

    var military = militaryMapper.toEntity(militaryDto);
    if (militaryDto.getRank() != null) {
      military.setRank(
          rankRepository.findByName(militaryDto.getRank()).orElseThrow(RankNotFoundException::new));
    }
    military.setSpecialties(specialtyRepository.findAllByCodeIn(militaryDto.getSpecialties()));

    var attributes = toEntities(militaryDto.getAttributes());
    attributes.forEach(attribute -> attribute.setMilitary(military));
    military.setAttributes(attributes);

    return militaryRepository.save(military);
  }

  @Transactional
  public Military update(@NonNull String mbn, @Valid @NonNull MilitaryDto militaryDto) {
    var military = militaryRepository.findByMbn(mbn).orElseThrow(MilitaryNotFoundException::new);

    militaryMapper.partialUpdate(militaryDto, military);
    if (militaryDto.getRank() != null) {
      military.setRank(
          rankRepository.findByName(militaryDto.getRank()).orElseThrow(RankNotFoundException::new));
    }
    military.setSpecialties(specialtyRepository.findAllByCodeIn(militaryDto.getSpecialties()));

    attributeRepository.deleteAllInBatch(military.getAttributes());
    var attributes = toEntities(militaryDto.getAttributes());
    attributes.forEach(attribute -> attribute.setMilitary(military));
    military.setAttributes(attributes);

    return militaryRepository.save(military);
  }

  @Transactional
  public long delete(@NonNull String mbn) {
    return militaryRepository.deleteByMbn(mbn);
  }

  private Specification<Military> generateSpecification(
      String firstName, String lastName, String middleName, String rank, String unit) {
    Specification<Military> spec = null;
    if (Objects.nonNull(firstName)) {
      spec = (root, query, builder) -> builder.like(root.get("firstName"), "%" + firstName + "%");
    }
    if (Objects.nonNull(lastName)) {
      Specification<Military> newSpec =
          (root, query, builder) -> builder.like(root.get("lastName"), "%" + lastName + "%");
      spec = Objects.isNull(spec) ? newSpec : spec.and(newSpec);
    }
    if (Objects.nonNull(middleName)) {
      Specification<Military> newSpec =
          (root, query, builder) -> builder.like(root.get("middleName"), "%" + middleName + "%");
      spec = Objects.isNull(spec) ? newSpec : spec.and(newSpec);
    }
    if (Objects.nonNull(rank)) {
      Specification<Military> newSpec =
          (root, query, builder) -> builder.like(root.get("rank").get("name"), "%" + rank + "%");
      spec = Objects.isNull(spec) ? newSpec : spec.and(newSpec);
    }
    if (Objects.nonNull(unit)) {
      Specification<Military> newSpec =
          (root, query, builder) -> builder.like(root.get("unit").get("name"), "%" + unit + "%");
      spec = Objects.isNull(spec) ? newSpec : spec.and(newSpec);
    }
    return spec;
  }

  private Set<Attribute> toEntities(Set<AttributeDto> attributeDtos) {
    return attributeDtos.stream().map(this::toEntity).collect(Collectors.toSet());
  }

  private Attribute toEntity(AttributeDto attributeDto) {
    var attribute = attributeMapper.toEntity(attributeDto);
    attribute.setRank(
        rankRepository.findByName(attributeDto.getRank()).orElseThrow(RankNotFoundException::new));
    return attribute;
  }

  @Override
  public Object resolveReference(@NonNull Map<String, Object> reference) {
    if (reference.get("mbn") instanceof String mbn) {
      return getByMbn(mbn);
    }
    return null;
  }
}
