package ru.nsu.ccfit.petrov.database.military_district.military;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MilitaryServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(MilitaryServiceApplication.class, args);
  }
}
