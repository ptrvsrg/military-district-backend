package ru.nsu.ccfit.petrov.database.military_district.military.persistence.repository;

import jakarta.persistence.QueryHint;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.QueryHints;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.entity.Specialty;

public interface SpecialtyRepository extends SpecPageSortRepository<Specialty, Integer> {

  @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
  Set<Specialty> findAllByCodeIn(Collection<String> codes);

  Optional<Specialty> findByCode(String code);
}
