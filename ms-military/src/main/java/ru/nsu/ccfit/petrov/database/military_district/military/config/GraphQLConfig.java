package ru.nsu.ccfit.petrov.database.military_district.military.config;

import com.apollographql.federation.graphqljava.Federation;
import com.apollographql.federation.graphqljava._Entity;
import com.apollographql.federation.graphqljava.tracing.FederatedTracingInstrumentation;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.graphql.GraphQlSourceBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.entity.Attribute;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.entity.Formation;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.entity.Military;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.entity.Rank;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.entity.RankCategory;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.entity.Specialty;
import ru.nsu.ccfit.petrov.database.military_district.military.service.AttributeDefinitionService;
import ru.nsu.ccfit.petrov.database.military_district.military.service.MilitaryService;
import ru.nsu.ccfit.petrov.database.military_district.military.service.RankCategoryService;
import ru.nsu.ccfit.petrov.database.military_district.military.service.RankService;
import ru.nsu.ccfit.petrov.database.military_district.military.service.SpecialtyService;

@Configuration
@RequiredArgsConstructor
public class GraphQLConfig {

  private final AttributeDefinitionService attributeDefinitionService;
  private final MilitaryService militaryService;
  private final RankCategoryService rankCategoryService;
  private final RankService rankService;
  private final SpecialtyService specialtyService;

  @Bean
  public FederatedTracingInstrumentation federatedTracingInstrumentation() {
    return new FederatedTracingInstrumentation();
  }

  @Bean
  public GraphQlSourceBuilderCustomizer federationTransform() {
    return builder ->
        builder.schemaFactory(
            (registry, wiring) ->
                Federation.transform(registry, wiring)
                    .fetchEntities(
                        env ->
                            env
                                .<List<Map<String, Object>>>getArgument(_Entity.argumentName)
                                .stream()
                                .map(
                                    reference -> {
                                      final String typeName = (String) reference.get("__typename");
                                      return switch (typeName) {
                                        case "MilitaryAttributeDefinition" ->
                                            attributeDefinitionService.resolveReference(reference);
                                        case "Military" ->
                                            militaryService.resolveReference(reference);
                                        case "RankCategory" ->
                                            rankCategoryService.resolveReference(reference);
                                        case "Rank" -> rankService.resolveReference(reference);
                                        case "Specialty" ->
                                            specialtyService.resolveReference(reference);
                                        default -> null;
                                      };
                                    })
                                .collect(Collectors.toList()))
                    .resolveEntityType(
                        env -> {
                          final Object src = env.getObject();
                          if (src instanceof Attribute) {
                            return env.getSchema().getObjectType("MilitaryAttribute");
                          } else if (src instanceof Formation) {
                            return env.getSchema().getObjectType("Unit");
                          } else if (src instanceof Military) {
                            return env.getSchema().getObjectType("Military");
                          } else if (src instanceof Rank) {
                            return env.getSchema().getObjectType("Rank");
                          } else if (src instanceof RankCategory) {
                            return env.getSchema().getObjectType("RankCategory");
                          } else if (src instanceof Specialty) {
                            return env.getSchema().getObjectType("Specialty");
                          } else {
                            return null;
                          }
                        })
                    .build());
  }
}
