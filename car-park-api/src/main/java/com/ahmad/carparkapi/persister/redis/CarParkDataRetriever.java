package com.ahmad.carparkapi.persister.redis;

import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;

public interface CarParkDataRetriever {

  Flux<CarParkGeoData> findWithin(double latitude, double longitude,
      Pageable pageable);
}
