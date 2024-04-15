package ru.nsu.ccfit.petrov.database.military_district.infrastructure.persistence.entity;

import static jakarta.persistence.FetchType.LAZY;
import static org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
@Table(name = "building_attributes")
@Cacheable
@Cache(usage = READ_WRITE)
public class Attribute {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String name;

  private String value;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "building_id")
  private Building building;
}