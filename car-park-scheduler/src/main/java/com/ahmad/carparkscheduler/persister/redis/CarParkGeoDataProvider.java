package com.ahmad.carparkscheduler.persister.redis;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CarParkGeoDataProvider implements CarParkGeoDataPersister {

  private final ReactiveRedisTemplate<String, CarParkGeoData> redisTemplate;

  @Value("${coordinate.collection-key}")
  private String collectionKey;

  public CarParkGeoDataProvider(
      @Qualifier("reactiveRedisTemplateConfig") ReactiveRedisTemplate<String, CarParkGeoData> redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  @Override
  public Mono<Boolean> addLocation(CarParkGeoData carParkGeoData) {
    return redisTemplate.opsForGeo().add(collectionKey, new Point(carParkGeoData.getLongitude(),
            carParkGeoData.getLatitude()), carParkGeoData)
        .map(l -> l > 0);
  }

  @Override
  public Mono<Long> deleteLocation() {
    return redisTemplate.delete(collectionKey);
  }
}
