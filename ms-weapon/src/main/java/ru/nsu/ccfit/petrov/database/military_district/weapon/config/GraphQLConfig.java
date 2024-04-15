package ru.nsu.ccfit.petrov.database.military_district.weapon.config;

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
import ru.nsu.ccfit.petrov.database.military_district.weapon.persistence.entity.Weapon;
import ru.nsu.ccfit.petrov.database.military_district.weapon.persistence.entity.WeaponAttribute;
import ru.nsu.ccfit.petrov.database.military_district.weapon.persistence.entity.WeaponCategory;
import ru.nsu.ccfit.petrov.database.military_district.weapon.persistence.entity.WeaponType;
import ru.nsu.ccfit.petrov.database.military_district.weapon.service.WeaponCategoryService;
import ru.nsu.ccfit.petrov.database.military_district.weapon.service.WeaponService;
import ru.nsu.ccfit.petrov.database.military_district.weapon.service.WeaponTypeService;

@Configuration
@RequiredArgsConstructor
public class GraphQLConfig {

  private final WeaponCategoryService weaponCategoryService;
  private final WeaponTypeService weaponTypeService;
  private final WeaponService weaponService;

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
                                        case "WeaponCategory" ->
                                            weaponCategoryService.resolveReference(reference);
                                        case "WeaponType" ->
                                            weaponTypeService.resolveReference(reference);
                                        case "Weapon" -> weaponService.resolveReference(reference);
                                        default -> null;
                                      };
                                    })
                                .collect(Collectors.toList()))
                    .resolveEntityType(
                        env -> {
                          final Object src = env.getObject();
                          if (src instanceof Weapon) {
                            return env.getSchema().getObjectType("Weapon");
                          } else if (src instanceof WeaponAttribute) {
                            return env.getSchema().getObjectType("WeaponAttribute");
                          } else if (src instanceof WeaponCategory) {
                            return env.getSchema().getObjectType("WeaponCategory");
                          } else if (src instanceof WeaponType) {
                            return env.getSchema().getObjectType("WeaponType");
                          } else {
                            return null;
                          }
                        })
                    .build());
  }
}
