package ru.nsu.ccfit.petrov.database.military_district.military.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.entity.Rank;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.repository.RankRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RankService implements GraphQLService {

  private final RankRepository rankRepository;

  public List<Rank> getAll(String category) {
    return rankRepository.findAll(generateSpecification(category));
  }

  public Rank getByName(@NonNull String name) {
    return rankRepository.findByName(name).orElse(null);
  }

  private Specification<Rank> generateSpecification(String category) {
    Specification<Rank> spec = null;
    if (Objects.nonNull(category)) {
      spec =
          (root, query, builder) ->
              builder.like(root.get("category").get("name"), "%" + category + "%");
    }
    return spec;
  }

  @Override
  public Object resolveReference(@NonNull Map<String, Object> reference) {
    if (reference.get("name") instanceof String name) {
      return getByName(name);
    }
    return null;
  }
}
