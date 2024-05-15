package ru.nsu.ccfit.petrov.database.military_district.military.service;

import java.util.List;
import java.util.Map;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.entity.Specialty;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.repository.SpecialtyRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class SpecialtyService implements GraphQLService {

  private final SpecialtyRepository specialtyRepository;

  @Cacheable(value = "specialties", unless = "#result.size() > 1000", sync = true)
  public List<Specialty> getAll() {
    log.info("Get all specialities");
    return specialtyRepository.findAll();
  }

  @Cacheable(value = "specialtyByCode", key = "#a0", sync = true)
  public Specialty getByCode(@NonNull String code) {
    log.info("Get specialty by code: code={}", code);
    return specialtyRepository.findByCode(code).orElse(null);
  }

  @Override
  @Cacheable(value = "reference", key = "#a0", sync = true)
  public Specialty resolveReference(@NonNull Map<String, Object> reference) {
    log.info("Resolve reference: reference={}", reference);
    if (reference.get("code") instanceof String code) {
      return specialtyRepository.findByCode(code).orElse(null);
    }
    return null;
  }
}
