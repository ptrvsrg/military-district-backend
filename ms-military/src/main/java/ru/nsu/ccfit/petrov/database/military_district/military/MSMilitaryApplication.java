package ru.nsu.ccfit.petrov.database.military_district.military;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MSMilitaryApplication {

  public static void main(String[] args) {
    SpringApplication.run(MSMilitaryApplication.class, args);
  }
}
