package ru.nsu.ccfit.petrov.database.military_district.formation.config;

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
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.entity.Address;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.entity.Army;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.entity.Brigade;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.entity.Company;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.entity.Coordinate;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.entity.Corps;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.entity.Division;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.entity.Platoon;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.entity.Squad;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.entity.Unit;
import ru.nsu.ccfit.petrov.database.military_district.formation.service.ArmyService;
import ru.nsu.ccfit.petrov.database.military_district.formation.service.BrigadeService;
import ru.nsu.ccfit.petrov.database.military_district.formation.service.CompanyService;
import ru.nsu.ccfit.petrov.database.military_district.formation.service.CorpsService;
import ru.nsu.ccfit.petrov.database.military_district.formation.service.DivisionService;
import ru.nsu.ccfit.petrov.database.military_district.formation.service.PlatoonService;
import ru.nsu.ccfit.petrov.database.military_district.formation.service.SquadService;
import ru.nsu.ccfit.petrov.database.military_district.formation.service.UnitService;

@Configuration
@RequiredArgsConstructor
public class GraphQLConfig {

  private final ArmyService armyService;
  private final BrigadeService brigadeService;
  private final CompanyService companyService;
  private final CorpsService corpsService;
  private final DivisionService divisionService;
  private final PlatoonService platoonService;
  private final SquadService squadService;
  private final UnitService unitService;

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
                                        case "Army" -> armyService.resolveReference(reference);
                                        case "Brigade" ->
                                            brigadeService.resolveReference(reference);
                                        case "Company" ->
                                            companyService.resolveReference(reference);
                                        case "Corps" -> corpsService.resolveReference(reference);
                                        case "Division" ->
                                            divisionService.resolveReference(reference);
                                        case "Platoon" ->
                                            platoonService.resolveReference(reference);
                                        case "Squad" -> squadService.resolveReference(reference);
                                        case "Unit" -> unitService.resolveReference(reference);
                                        default -> null;
                                      };
                                    })
                                .collect(Collectors.toList()))
                    .resolveEntityType(
                        env -> {
                          final Object src = env.getObject();
                          if (src instanceof Address) {
                            return env.getSchema().getObjectType("Address");
                          } else if (src instanceof Army) {
                            return env.getSchema().getObjectType("Army");
                          } else if (src instanceof Brigade) {
                            return env.getSchema().getObjectType("Brigade");
                          } else if (src instanceof Company) {
                            return env.getSchema().getObjectType("Company");
                          } else if (src instanceof Coordinate) {
                            return env.getSchema().getObjectType("Coordinate");
                          } else if (src instanceof Corps) {
                            return env.getSchema().getObjectType("Corps");
                          } else if (src instanceof Division) {
                            return env.getSchema().getObjectType("Division");
                          } else if (src instanceof Platoon) {
                            return env.getSchema().getObjectType("Platoon");
                          } else if (src instanceof Squad) {
                            return env.getSchema().getObjectType("Squad");
                          } else if (src instanceof Unit) {
                            return env.getSchema().getObjectType("Unit");
                          } else {
                            return null;
                          }
                        })
                    .build());
  }
}
