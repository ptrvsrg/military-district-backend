package ru.nsu.ccfit.petrov.database.military_district.formation.persistence.entity;

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
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "armies")
public class Army {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "name")
  private String name;

  @Embedded private Military commander;

  @CreationTimestamp
  @Column(name = "created_at")
  private Instant createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private Instant updatedAt;

  @ManyToMany
  @JoinTable(
      name = "armies_brigades",
      joinColumns = @JoinColumn(name = "army_id"),
      inverseJoinColumns = @JoinColumn(name = "brigade_id"))
  private Set<Brigade> brigades = new LinkedHashSet<>();

  @ManyToMany
  @JoinTable(
      name = "armies_corps",
      joinColumns = @JoinColumn(name = "army_id"),
      inverseJoinColumns = @JoinColumn(name = "corps_id"))
  private Set<Corps> corps = new LinkedHashSet<>();

  @ManyToMany
  @JoinTable(
      name = "armies_divisions",
      joinColumns = @JoinColumn(name = "army_id"),
      inverseJoinColumns = @JoinColumn(name = "division_id"))
  private Set<Division> divisions = new LinkedHashSet<>();
}
