package ru.nsu.ccfit.petrov.database.military_district.formation.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
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
@Table(name = "units")
public class Unit {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "name")
  private String name;

  @Embedded private Coordinate coordinate;

  @Embedded private Address address;

  @Embedded private Military commander;

  @CreationTimestamp
  @Column(name = "created_at")
  private Instant createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private Instant updatedAt;

  @ManyToMany(mappedBy = "units")
  private Set<Brigade> brigades = new LinkedHashSet<>();

  @OneToMany(mappedBy = "unit")
  private Set<Company> companies = new LinkedHashSet<>();

  @ManyToMany(mappedBy = "units")
  private Set<Corps> corps = new LinkedHashSet<>();

  @ManyToMany(mappedBy = "units")
  private Set<Division> divisions = new LinkedHashSet<>();
}
