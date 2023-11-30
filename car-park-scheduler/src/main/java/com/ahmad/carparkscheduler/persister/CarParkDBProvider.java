package com.ahmad.carparkscheduler.persister;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CarParkDBProvider implements CarParkInfoPersister, CarParkInfoRetriever{

  private final CarParkInfoRepository carParkInfoRepository;

  @Override
  public Mono<Void> saveCarParkInfo(Flux<CarParkInfoEntity> carParkInfoEntityFlux) {
    return carParkInfoRepository.saveAll(carParkInfoEntityFlux).then();
  }

  @Override
  public Mono<Long> count() {
    return carParkInfoRepository.count();
  }

  @Override
  public Flux<CarParkInfo> findAllCarParkInfo() {
    return carParkInfoRepository.findAll()
        .map(CarParkInfoConverter::fromEntity);
  }
}
