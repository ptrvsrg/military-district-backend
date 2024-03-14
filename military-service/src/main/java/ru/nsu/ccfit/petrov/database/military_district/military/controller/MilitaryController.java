package ru.nsu.ccfit.petrov.database.military_district.military.controller;

import jakarta.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.ccfit.petrov.database.military_district.common.PaginationInput;
import ru.nsu.ccfit.petrov.database.military_district.military.dto.CreateMilitaryInput;
import ru.nsu.ccfit.petrov.database.military_district.military.dto.MilitaryFilter;
import ru.nsu.ccfit.petrov.database.military_district.military.dto.MilitarySort;
import ru.nsu.ccfit.petrov.database.military_district.military.dto.UpdateMilitaryInput;
import ru.nsu.ccfit.petrov.database.military_district.military.exception.MilitaryAlreadyExistsException;
import ru.nsu.ccfit.petrov.database.military_district.military.exception.MilitaryNotFoundException;
import ru.nsu.ccfit.petrov.database.military_district.military.mapper.MilitaryAttributeMapper;
import ru.nsu.ccfit.petrov.database.military_district.military.mapper.MilitaryMapper;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.entity.Military;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.entity.MilitaryAttribute;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.repository.MilitaryAttributeRepository;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.repository.MilitaryRepository;

@Controller
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MilitaryController {

  private final MilitaryRepository militaryRepository;
  private final MilitaryAttributeRepository militaryAttributeRepository;
  private final MilitaryMapper militaryMapper;
  private final MilitaryAttributeMapper militaryAttributeMapper;

  @QueryMapping
  public List<Military> militaries(
      @Argument("filterInput") MilitaryFilter filterInput,
      @Argument("sortInput") MilitarySort sortInput,
      @Argument("paginationInput") PaginationInput paginationInput) {
    var sort = sortInput != null ? sortInput.generateSort() : Sort.unsorted();
    var pageable =
        paginationInput != null ? paginationInput.generatePageable().withSort(sort) : null;
    var spec = filterInput != null ? filterInput.generateSpecification() : null;

    if (spec == null && pageable == null) return militaryRepository.findAll(sort);
    if (spec == null) return militaryRepository.findAll(pageable).getContent();
    if (pageable == null) return militaryRepository.findAll(spec, sort);
    return militaryRepository.findAll(spec, pageable).getContent();
  }

  @QueryMapping
  public Military militaryByMbn(@Argument("mbn") String mbn) {
    return militaryRepository.findByMbn(mbn).orElse(null);
  }

  @MutationMapping
  @Transactional
  public Military createMilitary(
      @Argument("militaryInput") @Valid CreateMilitaryInput militaryInput) {
    if (militaryRepository.existsByMbn(militaryInput.getMbn())) {
      throw new MilitaryAlreadyExistsException();
    }
    var military = militaryMapper.toEntity(militaryInput);
    var attributes =
        militaryInput.getAttributes() != null
            ? militaryInput.getAttributes().stream()
                .map(attribute -> militaryAttributeMapper.toEntity(attribute, military))
                .collect(Collectors.toSet())
            : new HashSet<MilitaryAttribute>();
    military.setAttributes(attributes);
    return militaryRepository.save(military);
  }

  @MutationMapping
  @Transactional
  public Military updateMilitary(
      @Argument("mbn") String mbn,
      @Argument("militaryInput") @Valid UpdateMilitaryInput militaryInput) {
    var military = militaryRepository.findByMbn(mbn).orElseThrow(MilitaryNotFoundException::new);
    militaryAttributeRepository.deleteAllInBatch(military.getAttributes());

    militaryMapper.partialUpdate(militaryInput, military);
    var attributes =
            militaryInput.getAttributes() != null
                    ? militaryInput.getAttributes().stream()
                    .map(attribute -> militaryAttributeMapper.toEntity(attribute, military))
                    .collect(Collectors.toSet())
                    : new HashSet<MilitaryAttribute>();
    military.setAttributes(attributes);
    return militaryRepository.save(military);
  }

  @MutationMapping
  @Transactional
  public boolean deleteMilitary(@Argument("mbn") String mbn) {
    militaryRepository.deleteByMbn(mbn);
    return true;
  }

  @MutationMapping
  @Transactional
  public boolean deleteMilitariesByMbn(@Argument("mbns") List<String> mbns) {
    militaryRepository.deleteAllByMbnIn(mbns);
    return true;
  }
}
