package ru.nsu.ccfit.petrov.database.military_district.military.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nsu.ccfit.petrov.database.military_district.military.persistence.entity.MilitaryAttribute;

public interface MilitaryAttributeRepository extends JpaRepository<MilitaryAttribute, Integer> {

    void deleteByMilitaryMbnAndDefinitionNameAndDefinitionRankName(String military_mbn, String definition_name, String definition_rank_name);
}
