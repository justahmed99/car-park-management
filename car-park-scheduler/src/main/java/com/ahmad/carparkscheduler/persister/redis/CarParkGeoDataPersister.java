package com.ahmad.carparkscheduler.persister.redis;

import reactor.core.publisher.Mono;

public interface CarParkGeoDataPersister {

  Mono<Boolean> addLocation(CarParkGeoData carParkGeoData);

  Mono<Long> deleteLocation();
}
