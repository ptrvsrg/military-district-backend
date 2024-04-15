package ru.nsu.ccfit.petrov.database.military_district.formation.persistence.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Struct;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Struct(name = "address")
public class Address implements Serializable {

  @JsonProperty("post_code")
  private Integer postCode;

  private String country;

  private String state;

  private String locality;

  private String street;

  @JsonProperty("house_number")
  private String houseNumber;
}
