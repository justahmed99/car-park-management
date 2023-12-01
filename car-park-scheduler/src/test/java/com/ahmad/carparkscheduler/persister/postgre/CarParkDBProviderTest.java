package com.ahmad.carparkscheduler.persister.postgre;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class CarParkDBProviderTest {

  @InjectMocks
  private CarParkDBProvider carParkDBProvider;

  @Mock
  private CarParkInfoRepository carParkInfoRepository;

  @Test
  void saveCarParkInfo() {
    final Flux<CarParkInfoEntity> carParkInfoEntityFlux = Flux.fromIterable(
        List.of(
            CarParkInfoEntity.builder()
                .carParkNo("C1")
                .address("Way 1")
                .latitude(1.0)
                .longitude(2.0)
                .x(1000.0)
                .y(2000.)
                .build(),
            CarParkInfoEntity.builder()
                .carParkNo("C2")
                .address("Way 2")
                .latitude(1.1)
                .longitude(2.1)
                .x(1200.0)
                .y(2300.)
                .build()
        )
    );
    when(carParkInfoRepository.saveAll(carParkInfoEntityFlux)).thenReturn(carParkInfoEntityFlux);

    StepVerifier.create(carParkDBProvider.saveCarParkInfo(carParkInfoEntityFlux))
        .verifyComplete();
    verify(carParkInfoRepository).saveAll(carParkInfoEntityFlux);
  }

  @Test
  void count() {
    long expectedCount = 5L;
    when(carParkInfoRepository.count()).thenReturn(Mono.just(expectedCount));

    StepVerifier.create(carParkDBProvider.count())
        .expectNext(expectedCount)
        .verifyComplete();
  }

  @Test
  void findAllCarParkInfo() {
    final CarParkInfoEntity entity = CarParkInfoEntity.builder()
        .carParkNo("C1")
        .address("Way 1")
        .latitude(1.0)
        .longitude(2.0)
        .x(1000.0)
        .y(2000.0)
        .build();
    final CarParkInfo expectedInfo = CarParkInfo.builder()
        .carParkNo("C1")
        .address("Way 1")
        .latitude(1.0)
        .longitude(2.0)
        .build();
    when(carParkInfoRepository.findAll()).thenReturn(Flux.just(entity));

    StepVerifier.create(carParkDBProvider.findAllCarParkInfo())
        .expectNext(expectedInfo)
        .verifyComplete();
  }
}