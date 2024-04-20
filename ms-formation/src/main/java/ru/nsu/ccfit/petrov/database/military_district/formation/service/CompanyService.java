package ru.nsu.ccfit.petrov.database.military_district.formation.service;

import static ru.nsu.ccfit.petrov.database.military_district.formation.util.SpecPageSortUtils.generateCompanySpec;
import static ru.nsu.ccfit.petrov.database.military_district.formation.util.SpecPageSortUtils.generatePageable;
import static ru.nsu.ccfit.petrov.database.military_district.formation.util.SpecPageSortUtils.generateSort;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.CompanyFilter;
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.CompanyInput;
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.Pagination;
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.Sorting;
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
@Slf4j
public class CompanyService implements GraphQLService {

  private static final List<String> availableSortFields =
      List.of("commander.mbn", "name", "unit.name");

  private final CompanyRepository companyRepository;
  private final PlatoonRepository platoonRepository;
  private final UnitRepository unitRepository;
  private final CompanyMapper companyMapper;

  public List<Company> getAll(CompanyFilter filter, Pagination pagination, List<Sorting> sorts) {
    log.info("Get all companies: filter={}, pagination={}, sorts={}", filter, pagination, sorts);
    var sort = generateSort(sorts, availableSortFields);
    var pageable = generatePageable(pagination, sort);
    var spec = generateCompanySpec(filter);
    return companyRepository.findAll(spec, pageable, sort);
  }

  public long getAllCount(CompanyFilter filter) {
    log.info("Get all companies count: filter={}", filter);
    var spec = generateCompanySpec(filter);
    return companyRepository.count(spec);
  }

  public Company getByName(@NonNull String name) {
    log.info("Get company by name: name={}", name);
    return companyRepository.findByName(name).orElse(null);
  }

  @Transactional
  public Company create(@Valid @NonNull CompanyInput companyInput) {
    log.info("Create company: input={}", companyInput);
    if (companyRepository.existsByName(companyInput.getName())) {
      throw new CompanyAlreadyExistsException();
    }

    var company = companyMapper.toEntity(companyInput);
    company.setPlatoons(platoonRepository.findByNameIn(companyInput.getPlatoons()));
    company.setUnit(
        unitRepository.findByName(companyInput.getUnit()).orElseThrow(UnitNotFoundException::new));

    return companyRepository.save(company);
  }

  @Transactional
  public Company update(@NonNull String name, @Valid @NonNull CompanyInput companyInput) {
    log.info("Update company: name={}, input={}", name, companyInput);
    var company = companyRepository.findByName(name).orElseThrow(CompanyNotFoundException::new);
    if (!name.equals(companyInput.getName())
        && companyRepository.existsByName(companyInput.getName())) {
      throw new CompanyAlreadyExistsException();
    }

    companyMapper.partialUpdate(companyInput, company);
    company.setPlatoons(platoonRepository.findByNameIn(companyInput.getPlatoons()));
    company.setUnit(
        unitRepository.findByName(companyInput.getUnit()).orElseThrow(UnitNotFoundException::new));

    return companyRepository.save(company);
  }

  @Transactional
  public long delete(@NonNull String name) {
    log.info("Delete company: name={}", name);
    return companyRepository.deleteByName(name);
  }

  @Override
  public Object resolveReference(@NonNull Map<String, Object> reference) {
    log.info("Resolve reference: reference={}", reference);
    if (reference.get("name") instanceof String name) {
      return getByName(name);
    }
    return null;
  }
}
