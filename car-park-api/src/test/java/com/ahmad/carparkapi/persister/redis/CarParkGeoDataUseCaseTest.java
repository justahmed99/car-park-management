package com.ahmad.carparkapi.persister.redis;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ahmad.carparkapi.controller.CarParkGeoDataDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class CarParkGeoDataUseCaseTest {
  @InjectMocks
  private CarParkGeoDataUseCase carParkGeoDataUseCase;

  @Mock
  private CarParkDataRetriever mockRetriever;


  @Test
  void findWithin() {
    double latitude = 1.0;
    double longitude = 1.0;
    Pageable pageable = Pageable.unpaged();
    CarParkGeoData mockData = CarParkGeoData.builder()
        .carParkNo("C1")
        .latitude(1.3000)
        .longitude(103.7)
        .address("Way 1")
        .lotsAvailable(10)
        .build();
    CarParkGeoDataDTO mockDataDTO = CarParkGeoDataDTO.builder()
        .carParkNo("C1")
        .latitude(1.3000)
        .longitude(103.7)
        .address("Way 1")
        .lotsAvailable(10)
        .build();

    when(mockRetriever.findWithin(latitude, longitude, pageable))
        .thenReturn(Flux.just(mockData));

    Flux<CarParkGeoDataDTO> result = carParkGeoDataUseCase.findWithin(latitude, longitude, pageable);

    StepVerifier.create(result)
        .expectNext(mockDataDTO)
        .verifyComplete();

    verify(mockRetriever, times(1)).findWithin(latitude, longitude, pageable);
  }
}