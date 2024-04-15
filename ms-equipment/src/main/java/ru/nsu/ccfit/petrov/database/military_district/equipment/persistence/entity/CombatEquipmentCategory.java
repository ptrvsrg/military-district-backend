package ru.nsu.ccfit.petrov.database.military_district.equipment.persistence.entity;

import static jakarta.persistence.CascadeType.PERSIST;
import static org.hibernate.annotations.CacheConcurrencyStrategy.READ_ONLY;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.Immutable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "combat_equipment_categories")
@Immutable
@Cache(usage = READ_ONLY)
public class CombatEquipmentCategory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String name;

  @OneToMany(mappedBy = "category", cascade = PERSIST)
  private Set<CombatEquipmentType> combatEquipmentTypes = new LinkedHashSet<>();
}
