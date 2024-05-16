package ru.nsu.ccfit.petrov.database.military_district.infrastructure.persistence.entity;

import static jakarta.persistence.CascadeType.PERSIST;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "buildings")
public class Building {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String name;

  @Embedded private Address address;

  @Embedded private Coordinate coordinate;

  @Embedded
  @AttributeOverrides({@AttributeOverride(name = "name", column = @Column(name = "unit_name"))})
  private Formation unit;

  @CreationTimestamp
  @Column(name = "created_at")
  private Instant createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private Instant updatedAt;

  @OneToMany(mappedBy = "building", cascade = PERSIST)
  private Set<Attribute> attributes = new LinkedHashSet<>();

  @ElementCollection
  @CollectionTable(name = "buildings_companies", joinColumns = @JoinColumn(name = "building_id"))
  @AttributeOverrides({@AttributeOverride(name = "name", column = @Column(name = "company_name"))})
  private Set<Formation> companies = new LinkedHashSet<>();

  @ElementCollection
  @CollectionTable(name = "buildings_platoons", joinColumns = @JoinColumn(name = "building_id"))
  @AttributeOverrides({@AttributeOverride(name = "name", column = @Column(name = "platoon_name"))})
  private Set<Formation> platoons = new LinkedHashSet<>();

  @ElementCollection
  @CollectionTable(name = "buildings_squads", joinColumns = @JoinColumn(name = "building_id"))
  @AttributeOverrides({@AttributeOverride(name = "name", column = @Column(name = "squad_name"))})
  private Set<Formation> squads = new LinkedHashSet<>();
}
