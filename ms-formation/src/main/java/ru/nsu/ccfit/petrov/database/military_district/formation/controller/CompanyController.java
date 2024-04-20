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
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.CompanyFilter;
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.CompanyInput;
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.Pagination;
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.Sorting;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.entity.Company;
import ru.nsu.ccfit.petrov.database.military_district.formation.service.CompanyService;

@Controller
@RequiredArgsConstructor
public class CompanyController {

  private final CompanyService companyService;

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_FORMATIONS')")
  public List<Company> getCompanies(
      @Argument("filter") CompanyFilter filter,
      @Argument("pagination") Pagination pagination,
      @Argument("sorts") List<Sorting> sorts) {
    return companyService.getAll(filter, pagination, sorts);
  }

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_FORMATIONS')")
  public long getCompanyCount(@Argument("filter") CompanyFilter filter) {
    return companyService.getAllCount(filter);
  }

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_FORMATIONS')")
  public Company getCompany(@Argument("name") @NonNull String name) {
    return companyService.getByName(name);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_FORMATIONS')")
  public Company createCompany(@Argument("input") @Valid @NonNull CompanyInput companyInput) {
    return companyService.create(companyInput);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_FORMATIONS')")
  public Company updateCompany(
      @Argument("name") @NonNull String name,
      @Argument("input") @Valid @NonNull CompanyInput companyInput) {
    return companyService.update(name, companyInput);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_FORMATIONS')")
  public long deleteCompany(@Argument("name") @NonNull String name) {
    return companyService.delete(name);
  }
}
