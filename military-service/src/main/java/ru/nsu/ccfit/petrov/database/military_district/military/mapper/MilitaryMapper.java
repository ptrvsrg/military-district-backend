package ru.nsu.ccfit.petrov.database.military_district.military.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import ru.nsu.ccfit.petrov.database.military_district.military.dto.CreateMilitaryInput;
import ru.nsu.ccfit.petrov.database.military_district.military.dto.UpdateMilitaryInput;
import ru.nsu.ccfit.petrov.database.military_district.military.exception.RankNotFoundException;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.entity.Military;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.entity.Rank;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.repository.RankRepository;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class MilitaryMapper {

  @Autowired private RankRepository rankRepository;

  @Mapping(source = "rankName", target = "rank", qualifiedByName = "findRankByName")
  @Mapping(target = "attributes", ignore = true)
  public abstract Military toEntity(CreateMilitaryInput militaryInput);

  @Named("findRankByName")
  protected Rank findRankByName(String rankName) {
    return rankRepository.findByName(rankName).orElseThrow(RankNotFoundException::new);
  }

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  @Mapping(target = "attributes", ignore = true)
  public abstract Military partialUpdate(
      UpdateMilitaryInput militaryInput, @MappingTarget Military military);
}
