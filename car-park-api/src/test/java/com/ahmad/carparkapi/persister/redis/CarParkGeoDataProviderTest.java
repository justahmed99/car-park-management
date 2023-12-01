package com.ahmad.carparkapi.persister.redis;

import static org.mockito.Mockito.any;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands.GeoLocation;
import org.springframework.data.redis.core.ReactiveGeoOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class CarParkGeoDataProviderTest {

  @InjectMocks
  private CarParkGeoDataProvider provider;

  @Mock
  private ReactiveRedisTemplate<String, CarParkGeoData> mockRedisTemplate;

  @Mock
  private ReactiveGeoOperations<String, CarParkGeoData> mockGeoOperations;

  @BeforeEach
  void setUp() {
    setField(provider, "collectionKey", "coordinates");
    setField(provider, "radius", 1.0);
    when(mockRedisTemplate.opsForGeo()).thenReturn(mockGeoOperations);
  }

  @Test
  void testFindWithin() {
    // Mock setup
    double latitude = 1.0;
    double longitude = 1.0;
    String collectionKey = "coordinates";
    int page = 0;
    int size = 5;
    PageRequest pageable = PageRequest.of(page, size);

    final Circle within = new Circle(new Point(longitude, latitude),
        new Distance(1, Metrics.KILOMETERS));
    CarParkGeoData mockData = CarParkGeoData.builder()
        .carParkNo("C21")
        .lotsAvailable(12)
        .address("Way 1")
        .latitude(1.01)
        .longitude(1.02)
        .build();
    Flux<GeoResult<CarParkGeoData>> mockFlux = Flux.just(
        new GeoResult<>(mockData, new Distance(1, Metrics.KILOMETERS)));

    Flux<GeoResult<GeoLocation<CarParkGeoData>>> transformed = transformFlux(mockFlux);
    when(mockGeoOperations.radius(eq(collectionKey), eq(within))).thenReturn(transformed);

    // Test execution
    Flux<CarParkGeoData> result = provider.findWithin(latitude, longitude, pageable);

    // Verification
    StepVerifier.create(result)
        .expectNext(mockData)
        .verifyComplete();

    verify(mockGeoOperations, times(1)).radius(eq(collectionKey), any(Circle.class));
  }

  private Flux<GeoResult<GeoLocation<CarParkGeoData>>> transformFlux(
      Flux<GeoResult<CarParkGeoData>> originalFlux) {
    return originalFlux.map(geoResult -> {
      CarParkGeoData data = geoResult.getContent();
      Distance distance = geoResult.getDistance();

      // Assuming you can retrieve Point (latitude and longitude) from CarParkGeoData
      Point point = new Point(data.getLongitude(), data.getLatitude());

      // Creating a new GeoLocation object with CarParkGeoData and its Point
      GeoLocation<CarParkGeoData> geoLocation = new GeoLocation<>(data, point);

      // Returning a new GeoResult with the GeoLocation and the original distance
      return new GeoResult<>(geoLocation, distance);
    });
  }

}