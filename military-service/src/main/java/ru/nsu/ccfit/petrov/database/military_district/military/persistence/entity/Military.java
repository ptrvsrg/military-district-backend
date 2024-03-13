package ru.nsu.ccfit.petrov.database.military_district.military.persistence.entity;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.TemporalType.DATE;
import static org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedEntityGraphs;
import jakarta.persistence.NamedSubgraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "militaries")
@Cacheable
@Cache(usage = READ_WRITE)
@NamedEntityGraphs(
    @NamedEntityGraph(
        name = "military-with-rank-and-specialties",
        attributeNodes = {
          @NamedAttributeNode(value = "rank", subgraph = "rank-with-category"),
          @NamedAttributeNode(value = "specialties")
        },
        subgraphs =
            @NamedSubgraph(
                name = "rank-with-category",
                attributeNodes = @NamedAttributeNode("category"))))
public class Military {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String mbn;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "middle_name")
  private String middleName;

  @Temporal(DATE)
  @Column(name = "birth_date")
  private Date birthDate;

  @Column(name = "avatar_url")
  private String avatarUrl;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "rank_id")
  private Rank rank;

  @Column(name = "unit_number")
  private Integer unitNumber;

  @OneToMany(mappedBy = "military", cascade = ALL)
  private Set<MilitaryAttribute> attributes = new LinkedHashSet<>();

  @ManyToMany
  @JoinTable(
      name = "militaries_specialties",
      joinColumns = @JoinColumn(name = "military_id"),
      inverseJoinColumns = @JoinColumn(name = "specialty_id"))
  private Set<Specialty> specialties = new LinkedHashSet<>();
}
