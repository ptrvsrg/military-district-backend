package ru.nsu.ccfit.petrov.database.military_district.military.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.entity.Attribute;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.repository.AttributeRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttributeDefinitionService implements GraphQLService {

  private final AttributeRepository attributeRepository;

  public List<Attribute> getAll(String rank) {
    return attributeRepository.findAll(generateSpecification(rank));
  }

  public Attribute getByRankAndName(@NonNull String name, @NonNull String rank) {
    return attributeRepository.findByNameAndRank_Name(name, rank).orElse(null);
  }

  private Specification<Attribute> generateSpecification(String rank) {
    Specification<Attribute> spec = null;
    if (Objects.nonNull(rank)) {
      spec = (root, query, builder) -> builder.like(root.get("rank").get("name"), "%" + rank + "%");
    }
    return spec;
  }

  @Override
  public Object resolveReference(@NonNull Map<String, Object> reference) {
    if (reference.get("rank") instanceof String rank
        && reference.get("name") instanceof String name) {
      return getByRankAndName(name, rank);
    }
    return null;
  }
}
