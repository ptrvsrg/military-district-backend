package ru.nsu.ccfit.petrov.database.military_district.equipment.persistence.entity;

import static jakarta.persistence.CascadeType.ALL;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "combat_equipment_types")
public class CombatEquipmentType {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id")
  private CombatEquipmentCategory category;

  @OneToMany(mappedBy = "type")
  private Set<CombatEquipment> combatEquipments = new LinkedHashSet<>();

  @OneToMany(mappedBy = "type", cascade = ALL)
  private Set<CombatEquipmentAttribute> attributes = new LinkedHashSet<>();
}
