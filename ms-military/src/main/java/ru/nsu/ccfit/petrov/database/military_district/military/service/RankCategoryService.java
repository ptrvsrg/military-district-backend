package ru.nsu.ccfit.petrov.database.military_district.military.service;

import java.util.List;
import java.util.Map;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.entity.RankCategory;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.repository.RankCategoryRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class RankCategoryService implements GraphQLService {

  private final RankCategoryRepository rankCategoryRepository;

  @Cacheable(value = "rankCategories", unless = "#result.size() > 1000", sync = true)
  public List<RankCategory> getAll() {
    log.info("Get all rank categories");
    return rankCategoryRepository.findAll();
  }

  @Cacheable(value = "rankCategoryByName", key = "#a0", sync = true)
  public RankCategory getByName(@NonNull String name) {
    log.info("Get rank category by name: name={}", name);
    return rankCategoryRepository.findByName(name).orElse(null);
  }

  @Override
  @Cacheable(value = "reference", key = "#a0", sync = true)
  public RankCategory resolveReference(@NonNull Map<String, Object> reference) {
    log.info("Resolve reference: reference={}", reference);
    if (reference.get("name") instanceof String name) {
      return rankCategoryRepository.findByName(name).orElse(null);
    }
    return null;
  }
}
