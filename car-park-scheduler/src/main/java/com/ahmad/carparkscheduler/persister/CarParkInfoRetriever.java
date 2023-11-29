package com.ahmad.carparkscheduler.persister;

import reactor.core.publisher.Mono;

public interface CarParkInfoRetriever {
  Mono<Long> count();
}
