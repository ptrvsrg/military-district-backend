package ru.nsu.ccfit.petrov.database.military_district.military.service;

import java.util.List;
import java.util.Map;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.entity.Specialty;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.repository.SpecialtyRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SpecialtyService implements GraphQLService {

  private final SpecialtyRepository specialtyRepository;

  public List<Specialty> getAll() {
    return specialtyRepository.findAll();
  }

  public Specialty getByCode(String code) {
    return specialtyRepository.findByCode(code).orElse(null);
  }

  @Override
  public Object resolveReference(@NonNull Map<String, Object> reference) {
    if (reference.get("code") instanceof String code) {
      return getByCode(code);
    }
    return null;
  }
}