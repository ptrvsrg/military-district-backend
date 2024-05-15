package ru.nsu.ccfit.petrov.database.military_district.military.service;

import static ru.nsu.ccfit.petrov.database.military_district.military.util.SpecPageSortUtils.generateAttributeDefinitionSpec;

import java.util.List;
import java.util.Map;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.ccfit.petrov.database.military_district.military.dto.AttributeDefinitionFilter;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.entity.Attribute;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.repository.AttributeRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AttributeDefinitionService implements GraphQLService {

  private final AttributeRepository attributeRepository;

  @Cacheable(
      value = "attributeDefinitions",
      key = "(#a0 != null ? #a0 : 'null')",
      unless = "#result.size() > 1000",
      sync = true)
  public List<Attribute> getAll(AttributeDefinitionFilter filter) {
    log.info("Get all military attribute definitions: filter={}", filter);
    return attributeRepository.findAll(generateAttributeDefinitionSpec(filter));
  }

  @Cacheable(value = "attributeDefinitionByRankAndName", key = "#a0 + '_' + #a1", sync = true)
  public Attribute getByRankAndName(@NonNull String name, @NonNull String rank) {
    log.info("Get military attribute definition: name={}, rank={}", name, rank);
    return attributeRepository.findByNameAndRank_Name(name, rank).orElse(null);
  }

  @Override
  @Cacheable(value = "reference", key = "#a0", sync = true)
  public Attribute resolveReference(@NonNull Map<String, Object> reference) {
    log.info("Resolve reference: reference={}", reference);
    if (reference.get("rank") instanceof String rank
        && reference.get("name") instanceof String name) {
      return attributeRepository.findByNameAndRank_Name(name, rank).orElse(null);
    }
    return null;
  }
}
