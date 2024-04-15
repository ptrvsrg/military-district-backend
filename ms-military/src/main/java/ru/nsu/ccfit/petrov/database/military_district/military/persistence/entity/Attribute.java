package ru.nsu.ccfit.petrov.database.military_district.military.persistence.entity;

import static jakarta.persistence.FetchType.LAZY;
import static org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE;

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
@Table(name = "military_attributes")
@Cache(usage = READ_WRITE)
public class Attribute {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String name;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "rank_id")
  private Rank rank;

  private String value;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "military_id")
  private Military military;
}
