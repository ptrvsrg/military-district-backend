package ru.nsu.ccfit.petrov.database.military_district.report;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MSReportApplication {

  public static void main(String[] args) {
    SpringApplication.run(MSReportApplication.class, args);
  }
}
