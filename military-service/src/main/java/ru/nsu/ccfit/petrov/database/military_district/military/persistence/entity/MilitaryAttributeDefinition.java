package ru.nsu.ccfit.petrov.database.military_district.military.persistence.entity;

import static jakarta.persistence.FetchType.LAZY;
import static org.hibernate.annotations.CacheConcurrencyStrategy.READ_ONLY;

import jakarta.persistence.Entity;
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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.Immutable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "military_attribute_definitions")
@Immutable
@Cache(usage = READ_ONLY)
public class MilitaryAttributeDefinition {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String name;

  private String description;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "rank_id")
  private Rank rank;

  @OneToMany(mappedBy = "definition")
  private Set<MilitaryAttribute> militaryAttributes = new LinkedHashSet<>();
}
