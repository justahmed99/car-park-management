package com.ahmad.carparkapi.persister.redis;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class CarParkGeoDataProvider implements CarParkDataRetriever {

  private final ReactiveRedisTemplate<String, CarParkGeoData> redisTemplate;

  @Value("${coordinate.radius-in-km}")
  private Double radius;

  @Value("${coordinate.collection-key}")
  private String collectionKey;

  public CarParkGeoDataProvider(
      @Qualifier("reactiveRedisTemplateConfig") ReactiveRedisTemplate<String, CarParkGeoData> redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  @Override
  public Flux<CarParkGeoData> findWithin(final double latitude,
      final double longitude,
      Pageable pageable) {
    final Circle within = new Circle(new Point(longitude, latitude),
        new Distance(radius, Metrics.KILOMETERS));
    return redisTemplate.opsForGeo()
        .radius(collectionKey, within)
        .skip(pageable.getOffset())
        .take(pageable.getPageSize())
        .map(result -> result.getContent().getName());
  }
}
