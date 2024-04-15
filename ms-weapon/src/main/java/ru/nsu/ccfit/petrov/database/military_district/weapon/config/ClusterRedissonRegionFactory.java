package ru.nsu.ccfit.petrov.database.military_district.weapon.config;

import java.util.Map;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.hibernate.RedissonRegionFactory;

public class ClusterRedissonRegionFactory extends RedissonRegionFactory {

  @Override
  protected RedissonClient createRedissonClient(Map properties) {
    var config = new Config();
    config
        .useClusterServers()
        .addNodeAddress(
            ((String) properties.get("hibernate.cache.redisson.node_addresses")).split(","))
        .setPassword((String) properties.get("hibernate.cache.redisson.password"))
        .setCheckSlotsCoverage(false);
    return Redisson.create(config);
  }
}
