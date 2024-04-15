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
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.PlatoonDto;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.entity.Platoon;
import ru.nsu.ccfit.petrov.database.military_district.formation.service.PlatoonService;

@Controller
@RequiredArgsConstructor
public class PlatoonController {

  private final PlatoonService platoonService;

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_FORMATIONS')")
  public List<Platoon> getPlatoons(
      @Argument("name") String name,
      @Argument("commander") String commander,
      @Argument("company") String company,
      @Argument("page") Integer page,
      @Argument("pageSize") Integer pageSize,
      @Argument("sort") String sortField,
      @Argument("sortAsc") Boolean sortAsc) {
    return platoonService.getAll(name, commander, company, page, pageSize, sortField, sortAsc);
  }

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_FORMATIONS')")
  public long getPlatoonCount(
      @Argument("name") String name,
      @Argument("commander") String commander,
      @Argument("company") String company) {
    return platoonService.getAllCount(name, commander, company);
  }

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_FORMATIONS')")
  public Platoon getPlatoon(@Argument("name") @NonNull String name) {
    return platoonService.getByName(name);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_FORMATIONS')")
  public Platoon createPlatoon(@Argument("input") @Valid @NonNull PlatoonDto platoonDto) {
    return platoonService.create(platoonDto);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_FORMATIONS')")
  public Platoon updatePlatoon(
      @Argument("name") @NonNull String name,
      @Argument("input") @Valid @NonNull PlatoonDto platoonDto) {
    return platoonService.update(name, platoonDto);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_FORMATIONS')")
  public long deletePlatoon(@Argument("name") @NonNull String name) {
    return platoonService.delete(name);
  }
}
