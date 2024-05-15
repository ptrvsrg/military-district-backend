package ru.nsu.ccfit.petrov.database.military_district.equipment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MSEquipmentApplication {

  public static void main(String[] args) {
    SpringApplication.run(MSEquipmentApplication.class, args);
  }
}
