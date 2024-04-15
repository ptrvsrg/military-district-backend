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
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.CorpsDto;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.entity.Corps;
import ru.nsu.ccfit.petrov.database.military_district.formation.service.CorpsService;

@Controller
@RequiredArgsConstructor
public class CorpsController {

  private final CorpsService corpsService;

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_FORMATIONS')")
  public List<Corps> getCorps(
      @Argument("name") String name,
      @Argument("commander") String commander,
      @Argument("page") Integer page,
      @Argument("pageSize") Integer pageSize,
      @Argument("sort") String sortField,
      @Argument("sortAsc") Boolean sortAsc) {
    return corpsService.getAll(name, commander, page, pageSize, sortField, sortAsc);
  }

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_FORMATIONS')")
  public long getCorpsCount(
      @Argument("name") String name, @Argument("commander") String commander) {
    return corpsService.getAllCount(name, commander);
  }

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_FORMATIONS')")
  public Corps getOneCorps(@Argument("name") @NonNull String name) {
    return corpsService.getByName(name);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_FORMATIONS')")
  public Corps createCorps(@Argument("input") @Valid @NonNull CorpsDto corpsDto) {
    return corpsService.create(corpsDto);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_FORMATIONS')")
  public Corps updateCorps(
      @Argument("name") @NonNull String name,
      @Argument("input") @Valid @NonNull CorpsDto corpsDto) {
    return corpsService.update(name, corpsDto);
  }

  @MutationMapping
  @PreAuthorize("hasAuthority('WRITE_FORMATIONS')")
  public long deleteCorps(@Argument("name") @NonNull String name) {
    return corpsService.delete(name);
  }
}
