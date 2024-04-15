package ru.nsu.ccfit.petrov.database.military_district.formation.service;

import static ru.nsu.ccfit.petrov.database.military_district.formation.util.SpecPageSortUtils.generatePageable;
import static ru.nsu.ccfit.petrov.database.military_district.formation.util.SpecPageSortUtils.generateSort;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.ArmyDto;
import ru.nsu.ccfit.petrov.database.military_district.formation.exception.ArmyAlreadyExistsException;
import ru.nsu.ccfit.petrov.database.military_district.formation.exception.ArmyNotFoundException;
import ru.nsu.ccfit.petrov.database.military_district.formation.mapper.ArmyMapper;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.entity.Army;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.repository.ArmyRepository;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.repository.BrigadeRepository;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.repository.CorpsRepository;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.repository.DivisionRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArmyService implements GraphQLService {

  private static final List<String> availableSortFields = List.of("commander.mbn", "name");

  private final ArmyRepository armyRepository;
  private final BrigadeRepository brigadeRepository;
  private final CorpsRepository corpsRepository;
  private final DivisionRepository divisionRepository;
  private final ArmyMapper armyMapper;

  public List<Army> getAll(
      String name,
      String commander,
      Integer page,
      Integer pageSize,
      String sortField,
      Boolean sortAsc) {
    var sort = generateSort(sortField, sortAsc, availableSortFields);
    var pageable = generatePageable(page, pageSize, sort);
    var spec = generateSpecification(name, commander);
    return armyRepository.findAll(spec, pageable, sort);
  }

  public long getAllCount(String name, String commander) {
    var spec = generateSpecification(name, commander);
    if (spec == null) {
      return armyRepository.count();
    }
    return armyRepository.count(spec);
  }

  public Army getByName(@NonNull String name) {
    return armyRepository.findByName(name).orElse(null);
  }

  @Transactional
  public Army create(@Valid @NonNull ArmyDto armyDto) {
    if (armyRepository.existsByName(armyDto.getName())) {
      throw new ArmyAlreadyExistsException();
    }

    var army = armyMapper.toEntity(armyDto);
    army.setBrigades(brigadeRepository.findByNameIn(armyDto.getBrigades()));
    army.setCorps(corpsRepository.findByNameIn(armyDto.getCorps()));
    army.setDivisions(divisionRepository.findByNameIn(armyDto.getDivisions()));

    return armyRepository.save(army);
  }

  @Transactional
  public Army update(@NonNull String name, @Valid @NonNull ArmyDto armyDto) {
    var army = armyRepository.findByName(name).orElseThrow(ArmyNotFoundException::new);
    if (!name.equals(armyDto.getName()) && armyRepository.existsByName(armyDto.getName())) {
      throw new ArmyAlreadyExistsException();
    }

    armyMapper.partialUpdate(armyDto, army);
    army.setBrigades(brigadeRepository.findByNameIn(armyDto.getBrigades()));
    army.setCorps(corpsRepository.findByNameIn(armyDto.getCorps()));
    army.setDivisions(divisionRepository.findByNameIn(armyDto.getDivisions()));

    return armyRepository.save(army);
  }

  @Transactional
  public long delete(@NonNull String name) {
    return armyRepository.deleteByName(name);
  }

  private Specification<Army> generateSpecification(String name, String commander) {
    Specification<Army> spec = null;
    if (Objects.nonNull(name)) {
      spec = (root, query, builder) -> builder.like(root.get("name"), "%" + name + "%");
    }
    if (Objects.nonNull(commander)) {
      Specification<Army> newSpec =
          (root, query, builder) -> builder.like(root.get("commander.mbn"), "%" + commander + "%");
      spec = Objects.isNull(spec) ? newSpec : spec.and(newSpec);
    }
    return spec;
  }

  @Override
  public Object resolveReference(@NonNull Map<String, Object> reference) {
    if (reference.get("name") instanceof String name) {
      return getByName(name);
    }
    return null;
  }
}
