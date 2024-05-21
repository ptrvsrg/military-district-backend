package ru.nsu.ccfit.petrov.database.military_district.infrastructure.persistence.entity;

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
@Struct(
    name = "coordinate",
    attributes = {"lat", "lng"})
public class Coordinate implements Serializable {

  private double lat;
  private double lng;
}
