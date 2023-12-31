package com.ahmad.carparkscheduler.persister.postgre;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CarParkInfoRetriever {
  Mono<Long> count();
  Flux<CarParkInfo> findAllCarParkInfo();
}
