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
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.UnitDto;
import ru.nsu.ccfit.petrov.database.military_district.formation.exception.UnitAlreadyExistsException;
import ru.nsu.ccfit.petrov.database.military_district.formation.exception.UnitNotFoundException;
import ru.nsu.ccfit.petrov.database.military_district.formation.mapper.UnitMapper;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.entity.Unit;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.repository.BrigadeRepository;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.repository.CompanyRepository;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.repository.CorpsRepository;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.repository.DivisionRepository;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.repository.UnitRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UnitService implements GraphQLService {

  private static final List<String> availableSortFields =
      List.of("name", "commander.mbn", "address");

  private final UnitRepository unitRepository;
  private final BrigadeRepository brigadeRepository;
  private final CorpsRepository corpsRepository;
  private final DivisionRepository divisionRepository;
  private final CompanyRepository companyRepository;
  private final UnitMapper unitMapper;

  public List<Unit> getAll(
      String name,
      String address,
      String commander,
      Integer page,
      Integer pageSize,
      String sortField,
      Boolean sortAsc) {
    var sort = generateSort(sortField, sortAsc, availableSortFields);
    var pageable = generatePageable(page, pageSize, sort);
    var spec = generateSpecification(name, address, commander);
    return unitRepository.findAll(spec, pageable, sort);
  }

  public long getAllCount(String name, String commander, String address) {
    var spec = generateSpecification(name, address, commander);
    if (spec == null) {
      return unitRepository.count();
    }
    return unitRepository.count(spec);
  }

  public Unit getByName(@NonNull String name) {
    return unitRepository.findByName(name).orElse(null);
  }

  @Transactional
  public Unit create(@Valid @NonNull UnitDto unitDto) {
    if (unitRepository.existsByName(unitDto.getName())) {
      throw new UnitAlreadyExistsException();
    }

    var unit = unitMapper.toEntity(unitDto);
    unit.setBrigades(brigadeRepository.findByNameIn(unitDto.getBrigades()));
    unit.setCorps(corpsRepository.findByNameIn(unitDto.getCorps()));
    unit.setDivisions(divisionRepository.findByNameIn(unitDto.getDivisions()));
    unit.setCompanies(companyRepository.findByNameIn(unitDto.getCompanies()));

    return unitRepository.save(unit);
  }

  @Transactional
  public Unit update(@NonNull String name, @Valid @NonNull UnitDto unitDto) {
    var unit = unitRepository.findByName(name).orElseThrow(UnitNotFoundException::new);
    if (!name.equals(unitDto.getName()) && unitRepository.existsByName(unitDto.getName())) {
      throw new UnitAlreadyExistsException();
    }

    unitMapper.partialUpdate(unitDto, unit);
    unit.setBrigades(brigadeRepository.findByNameIn(unitDto.getBrigades()));
    unit.setCorps(corpsRepository.findByNameIn(unitDto.getCorps()));
    unit.setDivisions(divisionRepository.findByNameIn(unitDto.getDivisions()));
    unit.setCompanies(companyRepository.findByNameIn(unitDto.getCompanies()));

    return unitRepository.save(unit);
  }

  @Transactional
  public long delete(String name) {
    return unitRepository.deleteByName(name);
  }

  private Specification<Unit> generateSpecification(String name, String address, String commander) {
    Specification<Unit> spec = null;
    if (Objects.nonNull(name)) {
      spec = (root, query, builder) -> builder.like(root.get("name"), "%" + name + "%");
    }
    if (Objects.nonNull(address)) {
      Specification<Unit> newSpec =
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
    if (Objects.nonNull(commander)) {
      Specification<Unit> newSpec =
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
