package ru.nsu.ccfit.petrov.database.military_district.military.persistence.repository;

import jakarta.persistence.QueryHint;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.entity.Rank;

@Repository
public interface RankRepository extends JpaRepository<Rank, Integer> {

    @Override
    @NonNull
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    List<Rank> findAll();

    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    List<Rank> findAllByCategoryName(String categoryName);

    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    Optional<Rank> findByName(String name);
}
