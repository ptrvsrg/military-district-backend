package ru.nsu.ccfit.petrov.database.military_district.infrastructure.config;

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
import ru.nsu.ccfit.petrov.database.military_district.infrastructure.persistence.entity.Address;
import ru.nsu.ccfit.petrov.database.military_district.infrastructure.persistence.entity.Attribute;
import ru.nsu.ccfit.petrov.database.military_district.infrastructure.persistence.entity.Building;
import ru.nsu.ccfit.petrov.database.military_district.infrastructure.persistence.entity.Coordinate;
import ru.nsu.ccfit.petrov.database.military_district.infrastructure.service.BuildingService;

@Configuration
@RequiredArgsConstructor
public class GraphQLConfig {

  private final BuildingService buildingService;

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
                                        case "Building" ->
                                            buildingService.resolveReference(reference);
                                        default -> null;
                                      };
                                    })
                                .collect(Collectors.toList()))
                    .resolveEntityType(
                        env -> {
                          final Object src = env.getObject();
                          if (src instanceof Address) {
                            return env.getSchema().getObjectType("Address");
                          } else if (src instanceof Attribute) {
                            return env.getSchema().getObjectType("Attribute");
                          } else if (src instanceof Building) {
                            return env.getSchema().getObjectType("Building");
                          } else if (src instanceof Coordinate) {
                            return env.getSchema().getObjectType("Coordinate");
                          } else {
                            return null;
                          }
                        })
                    .build());
  }
}
