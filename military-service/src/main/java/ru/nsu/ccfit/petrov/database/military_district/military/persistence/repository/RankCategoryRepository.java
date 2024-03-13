package ru.nsu.ccfit.petrov.database.military_district.military.persistence.repository;

import jakarta.persistence.QueryHint;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.entity.RankCategory;

@Repository
public interface RankCategoryRepository extends JpaRepository<RankCategory, Integer> {
    
    @Override
    @NonNull
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    List<RankCategory> findAll();
}
