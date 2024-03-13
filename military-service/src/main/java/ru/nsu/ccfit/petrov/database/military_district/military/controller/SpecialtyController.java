package ru.nsu.ccfit.petrov.database.military_district.military.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.entity.Specialty;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.repository.SpecialtyRepository;

@Controller
@RequiredArgsConstructor
public class SpecialtyController {

  private final SpecialtyRepository specialtyRepository;

  @QueryMapping
  public List<Specialty> specialties() {
    return specialtyRepository.findAll();
  }
}
