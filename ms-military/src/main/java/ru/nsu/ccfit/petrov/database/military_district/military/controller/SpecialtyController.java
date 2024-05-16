package ru.nsu.ccfit.petrov.database.military_district.military.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.entity.Specialty;
import ru.nsu.ccfit.petrov.database.military_district.military.service.SpecialtyService;

@Controller
@RequiredArgsConstructor
public class SpecialtyController {

  private final SpecialtyService specialtyService;

  @QueryMapping
  @PreAuthorize("hasAuthority('READ_MILITARIES')")
  public List<Specialty> getSpecialties() {
    return specialtyService.getAll();
  }
}
