package ru.nsu.ccfit.petrov.database.military_district.formation.service;

import static ru.nsu.ccfit.petrov.database.military_district.formation.util.SpecPageSortUtils.generatePageable;
import static ru.nsu.ccfit.petrov.database.military_district.formation.util.SpecPageSortUtils.generateSort;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.ccfit.petrov.database.military_district.formation.dto.PlatoonDto;
import ru.nsu.ccfit.petrov.database.military_district.formation.exception.CompanyNotFoundException;
import ru.nsu.ccfit.petrov.database.military_district.formation.exception.PlatoonAlreadyExistsException;
import ru.nsu.ccfit.petrov.database.military_district.formation.exception.PlatoonNotFoundException;
import ru.nsu.ccfit.petrov.database.military_district.formation.mapper.PlatoonMapper;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.entity.Platoon;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.repository.CompanyRepository;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.repository.PlatoonRepository;
import ru.nsu.ccfit.petrov.database.military_district.formation.persistence.repository.SquadRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlatoonService implements GraphQLService {

  private static final List<String> availableSortFields =
      List.of("commander.mbn", "name", "company.name");

  private final PlatoonRepository platoonRepository;
  private final CompanyRepository companyRepository;
  private final SquadRepository squadRepository;
  private final PlatoonMapper platoonMapper;

  public List<Platoon> getAll(
      String name,
      String commander,
      String company,
      Integer page,
      Integer pageSize,
      String sortField,
      Boolean sortAsc) {
    var sort = generateSort(sortField, sortAsc, availableSortFields);
    var pageable = generatePageable(page, pageSize, sort);
    var spec = generateSpecification(name, commander, company);
    return platoonRepository.findAll(spec, pageable, sort);
  }

  public long getAllCount(String name, String commander, String company) {
    var spec = generateSpecification(name, commander, company);
    if (spec == null) {
      return platoonRepository.count();
    }
    return platoonRepository.count(spec);
  }

  public Platoon getByName(@NonNull String name) {
    return platoonRepository.findByName(name).orElse(null);
  }

  @Transactional
  public Platoon create(@Valid @NonNull PlatoonDto platoonDto) {
    if (platoonRepository.existsByName(platoonDto.getName())) {
      throw new PlatoonAlreadyExistsException();
    }

    var platoon = platoonMapper.toEntity(platoonDto);
    platoon.setSquads(squadRepository.findByNameIn(platoonDto.getSquads()));
    platoon.setCompany(
        companyRepository
            .findByName(platoonDto.getCompany())
            .orElseThrow(CompanyNotFoundException::new));

    return platoonRepository.save(platoon);
  }

  @Transactional
  public Platoon update(@NonNull String name, @Valid @NonNull PlatoonDto platoonDto) {
    var platoon = platoonRepository.findByName(name).orElseThrow(PlatoonNotFoundException::new);
    if (!name.equals(platoonDto.getName())
        && platoonRepository.existsByName(platoonDto.getName())) {
      throw new PlatoonAlreadyExistsException();
    }

    platoonMapper.partialUpdate(platoonDto, platoon);
    platoon.setSquads(squadRepository.findByNameIn(platoonDto.getSquads()));
    platoon.setCompany(
        companyRepository
            .findByName(platoonDto.getCompany())
            .orElseThrow(CompanyNotFoundException::new));

    return platoonRepository.save(platoon);
  }

  @Transactional
  public long delete(@NonNull String name) {
    return platoonRepository.deleteByName(name);
  }

  private Specification<Platoon> generateSpecification(
      String name, String commander, String company) {
    Specification<Platoon> spec = null;
    if (Objects.nonNull(name)) {
      spec = (root, query, builder) -> builder.like(root.get("name"), "%" + name + "%");
    }
    if (Objects.nonNull(commander)) {
      Specification<Platoon> newSpec =
          (root, query, builder) -> builder.like(root.get("commander.mbn"), "%" + commander + "%");
      spec = Objects.isNull(spec) ? newSpec : spec.and(newSpec);
    }
    if (Objects.nonNull(company)) {
      Specification<Platoon> newSpec =
          (root, query, builder) -> builder.like(root.get("company.name"), "%" + company + "%");
      spec = Objects.isNull(spec) ? newSpec : spec.and(newSpec);
    }
    return spec;
  }

  @Override
  public Object resolveReference(@NonNull Map<String, Object> reference) {
    if (reference.get("name") instanceof String name) {
      return getByName(name);
    }
    return null;
  }
}
