package com.ahmad.carparkscheduler.persister;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CarParkInfoPersister {
  Mono<Void> saveCarParkInfo(Flux<CarParkInfoEntity> carParkInfoEntityFlux);
}
