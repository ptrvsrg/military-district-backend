package ru.nsu.ccfit.petrov.database.military_district.formation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MSFormationApplication {

  public static void main(String[] args) {
    SpringApplication.run(MSFormationApplication.class, args);
  }
}
