package ru.nsu.ccfit.petrov.database.military_district.equipment.persistence.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.lang.NonNull;

@NoRepositoryBean
public interface SpecPageSortRepository<T, ID>
    extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

  @Override
  long count(@NonNull Specification<T> spec);

  @Override
  long count();

  @Override
  @NonNull
  Page<T> findAll(@NonNull Specification<T> spec, @NonNull Pageable pageable);

  @Override
  @NonNull
  List<T> findAll(@NonNull Specification<T> spec, @NonNull Sort sort);

  @Override
  @NonNull
  List<T> findAll(@NonNull Specification spec);

  @Override
  @NonNull
  List<T> findAll(@NonNull Sort sort);

  @Override
  @NonNull
  Page<T> findAll(@NonNull Pageable pageable);

  default List<T> findAll(Specification<T> spec, Pageable pageable, Sort sort) {
    if (sort == null) {
      sort = Sort.unsorted();
    }
    if (spec == null && pageable == null) {
      return findAll(sort);
    }
    if (spec == null) {
      return findAll(pageable).getContent();
    }
    if (pageable == null) {
      return findAll(spec, sort);
    }
    return findAll(spec, pageable).getContent();
  }
}
