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
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.BrigadeDto;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.entity.Brigade;
import ru.nsu.ccfit.petrov.database.military_district.formation.service.BrigadeService;

@Controller
@RequiredArgsConstructor
public class BrigadeController {

  private final BrigadeService brigadeService;

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_FORMATIONS')")
  public List<Brigade> getBrigades(
      @Argument("name") String name,
      @Argument("commander") String commander,
      @Argument("page") Integer page,
      @Argument("pageSize") Integer pageSize,
      @Argument("sort") String sortField,
      @Argument("sortAsc") Boolean sortAsc) {
    return brigadeService.getAll(name, commander, page, pageSize, sortField, sortAsc);
  }

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_FORMATIONS')")
  public long getBrigadeCount(
      @Argument("name") String name, @Argument("commander") String commander) {
    return brigadeService.getAllCount(name, commander);
  }

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_FORMATIONS')")
  public Brigade getBrigade(@Argument("name") @NonNull String name) {
    return brigadeService.getByName(name);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_FORMATIONS')")
  public Brigade createBrigade(@Argument("input") @Valid @NonNull BrigadeDto brigadeDto) {
    return brigadeService.create(brigadeDto);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_FORMATIONS')")
  public Brigade updateBrigade(
      @Argument("name") @NonNull String name,
      @Argument("input") @Valid @NonNull BrigadeDto brigadeDto) {
    return brigadeService.update(name, brigadeDto);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_FORMATIONS')")
  public long deleteBrigade(@Argument("name") @NonNull String name) {
    return brigadeService.delete(name);
  }
}
