package ru.nsu.ccfit.petrov.database.military_district.infrastructure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MSInfrastructureApplication {

  public static void main(String[] args) {
    SpringApplication.run(MSInfrastructureApplication.class, args);
  }
}
