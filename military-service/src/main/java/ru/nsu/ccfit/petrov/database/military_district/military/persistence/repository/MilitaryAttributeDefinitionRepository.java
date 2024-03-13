package ru.nsu.ccfit.petrov.database.military_district.military.persistence.repository;

import jakarta.persistence.QueryHint;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.entity.MilitaryAttributeDefinition;

@Repository
public interface MilitaryAttributeDefinitionRepository
    extends JpaRepository<MilitaryAttributeDefinition, Integer> {

    @Override
    @NonNull
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    List<MilitaryAttributeDefinition> findAll();

    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    List<MilitaryAttributeDefinition> findAllByRankName(String rankName);

    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    Optional<MilitaryAttributeDefinition> findByNameAndRankName(String name, String rankName);
}
