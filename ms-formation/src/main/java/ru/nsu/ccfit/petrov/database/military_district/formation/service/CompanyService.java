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
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.CompanyDto;
import ru.nsu.ccfit.petrov.database.military_district.formation.exception.CompanyAlreadyExistsException;
import ru.nsu.ccfit.petrov.database.military_district.formation.exception.CompanyNotFoundException;
import ru.nsu.ccfit.petrov.database.military_district.formation.exception.UnitNotFoundException;
import ru.nsu.ccfit.petrov.database.military_district.formation.mapper.CompanyMapper;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.entity.Company;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.repository.CompanyRepository;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.repository.PlatoonRepository;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.repository.UnitRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompanyService implements GraphQLService {

  private static final List<String> availableSortFields =
      List.of("commander.mbn", "name", "unit.name");

  private final CompanyRepository companyRepository;
  private final PlatoonRepository platoonRepository;
  private final UnitRepository unitRepository;
  private final CompanyMapper companyMapper;

  public List<Company> getAll(
      String name,
      String commander,
      String unit,
      Integer page,
      Integer pageSize,
      String sortField,
      Boolean sortAsc) {
    var sort = generateSort(sortField, sortAsc, availableSortFields);
    var pageable = generatePageable(page, pageSize, sort);
    var spec = generateSpecification(name, commander, unit);
    return companyRepository.findAll(spec, pageable, sort);
  }

  public long getAllCount(String name, String commander, String unit) {
    var spec = generateSpecification(name, commander, unit);
    if (spec == null) {
      return companyRepository.count();
    }
    return companyRepository.count(spec);
  }

  public Company getByName(@NonNull String name) {
    return companyRepository.findByName(name).orElse(null);
  }

  @Transactional
  public Company create(@Valid @NonNull CompanyDto companyDto) {
    if (companyRepository.existsByName(companyDto.getName())) {
      throw new CompanyAlreadyExistsException();
    }

    var company = companyMapper.toEntity(companyDto);
    company.setPlatoons(platoonRepository.findByNameIn(companyDto.getPlatoons()));
    company.setUnit(
        unitRepository.findByName(companyDto.getUnit()).orElseThrow(UnitNotFoundException::new));

    return companyRepository.save(company);
  }

  @Transactional
  public Company update(@NonNull String name, @Valid @NonNull CompanyDto companyDto) {
    var company = companyRepository.findByName(name).orElseThrow(CompanyNotFoundException::new);
    if (!name.equals(companyDto.getName())
        && companyRepository.existsByName(companyDto.getName())) {
      throw new CompanyAlreadyExistsException();
    }

    companyMapper.partialUpdate(companyDto, company);
    company.setPlatoons(platoonRepository.findByNameIn(companyDto.getPlatoons()));
    company.setUnit(
        unitRepository.findByName(companyDto.getUnit()).orElseThrow(UnitNotFoundException::new));

    return companyRepository.save(company);
  }

  @Transactional
  public long delete(@NonNull String name) {
    return companyRepository.deleteByName(name);
  }

  private Specification<Company> generateSpecification(String name, String commander, String unit) {
    Specification<Company> spec = null;
    if (Objects.nonNull(name)) {
      spec = (root, query, builder) -> builder.like(root.get("name"), "%" + name + "%");
    }
    if (Objects.nonNull(commander)) {
      Specification<Company> newSpec =
          (root, query, builder) -> builder.like(root.get("commander.mbn"), "%" + commander + "%");
      spec = Objects.isNull(spec) ? newSpec : spec.and(newSpec);
    }
    if (Objects.nonNull(unit)) {
      Specification<Company> newSpec =
          (root, query, builder) -> builder.like(root.get("unit.name"), "%" + unit + "%");
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
