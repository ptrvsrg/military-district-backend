package ru.nsu.ccfit.petrov.database.military_district.military.persistence.repository;

import jakarta.persistence.QueryHint;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.entity.Military;

@Repository
public interface MilitaryRepository
    extends JpaRepository<Military, Integer>, JpaSpecificationExecutor<Military> {

  @Override
  @NonNull
  @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
  @EntityGraph("military-with-rank-and-specialties")
  Page<Military> findAll(@NonNull Specification<Military> spec, @NonNull Pageable pageable);

  @Override
  @NonNull
  @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
  @EntityGraph("military-with-rank-and-specialties")
  List<Military> findAll(@NonNull Specification<Military> spec, @NonNull Sort sort);

  @Override
  @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
  @EntityGraph("military-with-rank-and-specialties")
  @NonNull
  List<Military> findAll(@NonNull Sort sort);

  @Override
  @NonNull
  @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
  @EntityGraph("military-with-rank-and-specialties")
  Page<Military> findAll(@NonNull Pageable pageable);

  @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
  @EntityGraph("military-with-rank-and-specialties")
  Optional<Military> findByMbn(String mbn);

  @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
  boolean existsByMbn(String mbn);

  void deleteByMbn(String mbn);

  void deleteAllByMbnIn(List<String> mbn);
}
