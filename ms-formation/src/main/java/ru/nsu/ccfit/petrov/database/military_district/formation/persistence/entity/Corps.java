package ru.nsu.ccfit.petrov.database.military_district.formation.persistence.entity;

import static org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "corps")
@Cacheable
@Cache(usage = READ_WRITE)
public class Corps {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "name")
  private String name;

  @Embedded private Military commander;

  @Column(name = "created_at")
  private Instant createdAt;

  @Column(name = "updated_at")
  private Instant updatedAt;

  @ManyToMany(mappedBy = "corps")
  private Set<Army> armies = new LinkedHashSet<>();

  @ManyToMany
  @JoinTable(
      name = "corps_units",
      joinColumns = @JoinColumn(name = "corps_id"),
      inverseJoinColumns = @JoinColumn(name = "unit_id"))
  private Set<Unit> units = new LinkedHashSet<>();
}
