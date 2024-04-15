package ru.nsu.ccfit.petrov.database.military_district.military.service;

import java.util.List;
import java.util.Map;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.entity.RankCategory;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.repository.RankCategoryRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RankCategoryService implements GraphQLService {

  private final RankCategoryRepository rankCategoryRepository;

  public List<RankCategory> getAll() {
    return rankCategoryRepository.findAll();
  }

  public RankCategory getByName(@NonNull String name) {
    return rankCategoryRepository.findByName(name).orElse(null);
  }

  @Override
  public Object resolveReference(@NonNull Map<String, Object> reference) {
    if (reference.get("name") instanceof String name) {
      return getByName(name);
    }
    return null;
  }
}
