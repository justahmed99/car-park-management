package com.ahmad.carparkscheduler.persister.redis;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CarParkGeoDataService {

  private final ReactiveRedisTemplate<String, CarParkGeoData> redisTemplate;

  public CarParkGeoDataService(
      @Qualifier("reactiveRedisTemplateConfig") ReactiveRedisTemplate<String, CarParkGeoData> redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  public Mono<Boolean> addLocation(String key, CarParkGeoData carParkGeoData) {
    return redisTemplate.opsForGeo().add(key, new Point(carParkGeoData.getLongitude(),
            carParkGeoData.getLatitude()), carParkGeoData)
        .map(l -> l > 0);
  }

  public Flux<CarParkGeoData> findWithin(String key, double latitude, double longitude, double radius) {
    Circle within = new Circle(new Point(longitude, latitude),
        new Distance(radius, Metrics.KILOMETERS));
    return redisTemplate.opsForGeo().radius(key, within)
        .map(result -> result.getContent().getName());
  }

  public Mono<Long> deleteLocation(String key) {
    return redisTemplate.delete(key);
  }
}
