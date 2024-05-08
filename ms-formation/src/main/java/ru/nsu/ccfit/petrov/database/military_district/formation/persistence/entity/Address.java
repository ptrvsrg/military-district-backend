package ru.nsu.ccfit.petrov.database.military_district.formation.persistence.entity;

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
@Struct(name = "address", attributes = {"post_code", "country", "state", "locality", "street", "house_number"})
public class Address implements Serializable {

  private Integer postCode;

  private String country;

  private String state;

  private String locality;

  private String street;

  private String houseNumber;
}
