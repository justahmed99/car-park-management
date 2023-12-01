package com.ahmad.carparkscheduler.persister.redis;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.ReactiveGeoOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class CarParkGeoDataProviderTest {

  @InjectMocks
  private CarParkGeoDataProvider carParkGeoDataProvider;

  @Mock
  private ReactiveRedisTemplate<String, CarParkGeoData> reactiveRedisTemplate;

  @Mock
  private ReactiveGeoOperations<String, CarParkGeoData> reactiveGeoOperations;

  @BeforeEach
  void setUp() {
    setField(carParkGeoDataProvider, "collectionKey", "coordinates");
  }

  @Test
  void addLocation() {
    CarParkGeoData geoData = CarParkGeoData.builder()
        .carParkNo("C1")
        .latitude(1.3000)
        .longitude(103.7)
        .address("Way 1")
        .lotsAvailable(10)
        .build();
    when(reactiveRedisTemplate.opsForGeo()).thenReturn(reactiveGeoOperations);
    when(reactiveGeoOperations.add("coordinates", new Point(geoData.getLongitude(), geoData.getLatitude()), geoData))
        .thenReturn(Mono.just(1L));

    Mono<Boolean> result = carParkGeoDataProvider.addLocation(geoData);

    StepVerifier.create(result)
        .expectNext(true)
        .verifyComplete();

    verify(reactiveGeoOperations, times(1)).add(eq("coordinates"), any(Point.class), eq(geoData));
  }

  @Test
  void deleteLocation() {
    when(reactiveRedisTemplate.delete(anyString())).thenReturn(Mono.just(1L));

    Mono<Long> result = carParkGeoDataProvider.deleteLocation();

    StepVerifier.create(result)
        .expectNext(1L)
        .verifyComplete();

    verify(reactiveRedisTemplate, times(1)).delete(anyString());
  }
}