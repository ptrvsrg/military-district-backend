package ru.nsu.ccfit.petrov.database.military_district.weapon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MSWeaponApplication {

  public static void main(String[] args) {
    SpringApplication.run(MSWeaponApplication.class, args);
  }
}
