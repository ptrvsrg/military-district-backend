package ru.nsu.ccfit.petrov.database.military_district.military.persistence.entity;

import static org.hibernate.annotations.CacheConcurrencyStrategy.READ_ONLY;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
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
@Table(name = "specialties")
@Immutable
@Cache(usage = READ_ONLY)
public class Specialty {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String code;

  private String name;

  @ManyToMany(mappedBy = "specialties")
  private Set<Military> militaries = new LinkedHashSet<>();
}
