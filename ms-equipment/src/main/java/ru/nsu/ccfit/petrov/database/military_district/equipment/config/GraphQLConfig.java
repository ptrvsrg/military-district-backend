package ru.nsu.ccfit.petrov.database.military_district.equipment.config;

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
import ru.nsu.ccfit.petrov.database.military_district.equipment.persistence.entity.CombatEquipment;
import ru.nsu.ccfit.petrov.database.military_district.equipment.persistence.entity.CombatEquipmentAttribute;
import ru.nsu.ccfit.petrov.database.military_district.equipment.persistence.entity.CombatEquipmentCategory;
import ru.nsu.ccfit.petrov.database.military_district.equipment.persistence.entity.CombatEquipmentType;
import ru.nsu.ccfit.petrov.database.military_district.equipment.service.CombatEquipmentCategoryService;
import ru.nsu.ccfit.petrov.database.military_district.equipment.service.CombatEquipmentService;
import ru.nsu.ccfit.petrov.database.military_district.equipment.service.CombatEquipmentTypeService;

@Configuration
@RequiredArgsConstructor
public class GraphQLConfig {

  private final CombatEquipmentCategoryService combatEquipmentCategoryService;
  private final CombatEquipmentTypeService combatEquipmentTypeService;
  private final CombatEquipmentService combatEquipmentService;

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
                                        case "CombatEquipmentCategory" ->
                                            combatEquipmentCategoryService.resolveReference(
                                                reference);
                                        case "CombatEquipmentType" ->
                                            combatEquipmentTypeService.resolveReference(reference);
                                        case "CombatEquipment" ->
                                            combatEquipmentService.resolveReference(reference);
                                        default -> null;
                                      };
                                    })
                                .collect(Collectors.toList()))
                    .resolveEntityType(
                        env -> {
                          final Object src = env.getObject();
                          if (src instanceof CombatEquipment) {
                            return env.getSchema().getObjectType("CombatEquipment");
                          } else if (src instanceof CombatEquipmentAttribute) {
                            return env.getSchema().getObjectType("CombatEquipmentAttribute");
                          } else if (src instanceof CombatEquipmentCategory) {
                            return env.getSchema().getObjectType("CombatEquipmentCategory");
                          } else if (src instanceof CombatEquipmentType) {
                            return env.getSchema().getObjectType("CombatEquipmentType");
                          } else {
                            return null;
                          }
                        })
                    .build());
  }
}
