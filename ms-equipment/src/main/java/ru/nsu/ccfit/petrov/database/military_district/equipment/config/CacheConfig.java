package ru.nsu.ccfit.petrov.database.military_district.equipment.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.time.Duration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig {

  @Bean
  public Caffeine<Object, Object> caffeineCacheBuilder() {
    return Caffeine.newBuilder().initialCapacity(100).expireAfterWrite(Duration.ofMinutes(5));
  }

  @Bean
  public CacheManager cacheManager() {
    CaffeineCacheManager cacheManager = new CaffeineCacheManager();
    cacheManager.setCaffeine(caffeineCacheBuilder());
    return cacheManager;
  }
}
