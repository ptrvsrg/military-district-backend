package ru.nsu.ccfit.petrov.database.military_district.military.service;

import static ru.nsu.ccfit.petrov.database.military_district.military.util.SpecPageSortUtils.generateRankSpec;

import java.util.List;
import java.util.Map;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.ccfit.petrov.database.military_district.military.dto.RankFilter;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.entity.Rank;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.repository.RankRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class RankService implements GraphQLService {

  private final RankRepository rankRepository;

  @Cacheable(
      value = "ranks",
      key = "(#a0 != null ? #a0 : 'null')",
      unless = "#result.size() > 1000")
  public List<Rank> getAll(RankFilter filter) {
    log.info("Get all ranks: filter={}", filter);
    return rankRepository.findAll(generateRankSpec(filter));
  }

  @Cacheable(value = "rankByName", key = "#a0", sync = true)
  public Rank getByName(@NonNull String name) {
    log.info("Get rank by name: name={}", name);
    return rankRepository.findByName(name).orElse(null);
  }

  @Override
  @Cacheable(value = "reference", key = "#a0", sync = true)
  public Rank resolveReference(@NonNull Map<String, Object> reference) {
    log.info("Resolve reference: reference={}", reference);
    if (reference.get("name") instanceof String name) {
      return rankRepository.findByName(name).orElse(null);
    }
    return null;
  }
}
