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
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.BrigadeFilter;
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.BrigadeInput;
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.Pagination;
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.Sorting;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.entity.Brigade;
import ru.nsu.ccfit.petrov.database.military_district.formation.service.BrigadeService;

@Controller
@RequiredArgsConstructor
public class BrigadeController {

  private final BrigadeService brigadeService;

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_FORMATIONS')")
  public List<Brigade> getBrigades(
      @Argument("filter") BrigadeFilter filter,
      @Argument("pagination") Pagination pagination,
      @Argument("sorts") List<Sorting> sorts) {
    return brigadeService.getAll(filter, pagination, sorts);
  }

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_FORMATIONS')")
  public long getBrigadeCount(@Argument("filter") BrigadeFilter filter) {
    return brigadeService.getAllCount(filter);
  }

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_FORMATIONS')")
  public Brigade getBrigade(@Argument("name") @NonNull String name) {
    return brigadeService.getByName(name);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_FORMATIONS')")
  public Brigade createBrigade(@Argument("input") @Valid @NonNull BrigadeInput brigadeInput) {
    return brigadeService.create(brigadeInput);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_FORMATIONS')")
  public Brigade updateBrigade(
      @Argument("name") @NonNull String name,
      @Argument("input") @Valid @NonNull BrigadeInput brigadeInput) {
    return brigadeService.update(name, brigadeInput);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_FORMATIONS')")
  public long deleteBrigade(@Argument("name") @NonNull String name) {
    return brigadeService.delete(name);
  }
}
