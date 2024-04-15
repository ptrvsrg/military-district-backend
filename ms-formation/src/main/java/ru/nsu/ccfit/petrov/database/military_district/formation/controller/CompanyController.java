package ru.nsu.ccfit.petrov.database.military_district.formation.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.CompanyDto;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.entity.Company;
import ru.nsu.ccfit.petrov.database.military_district.formation.service.CompanyService;

@Controller
@RequiredArgsConstructor
public class CompanyController {

  private final CompanyService companyService;

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_FORMATIONS')")
  public List<Company> getCompanies(
      @Argument("name") String name,
      @Argument("commander") String commander,
      @Argument("unit") String unit,
      @Argument("page") Integer page,
      @Argument("pageSize") Integer pageSize,
      @Argument("sort") String sortField,
      @Argument("sortAsc") Boolean sortAsc) {
    return companyService.getAll(name, commander, unit, page, pageSize, sortField, sortAsc);
  }

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_FORMATIONS')")
  public long getCompanyCount(
      @Argument("name") String name,
      @Argument("commander") String commander,
      @Argument("unit") String unit) {
    return companyService.getAllCount(name, commander, unit);
  }

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_FORMATIONS')")
  public Company getCompany(@Argument("name") @NonNull String name) {
    return companyService.getByName(name);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_FORMATIONS')")
  public Company createCompany(@Argument("input") @Valid @NonNull CompanyDto companyDto) {
    return companyService.create(companyDto);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_FORMATIONS')")
  public Company updateCompany(
      @Argument("name") @NonNull String name,
      @Argument("input") @Valid @NonNull CompanyDto companyDto) {
    return companyService.update(name, companyDto);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_FORMATIONS')")
  public long deleteCompany(@Argument("name") @NonNull String name) {
    return companyService.delete(name);
  }
}
