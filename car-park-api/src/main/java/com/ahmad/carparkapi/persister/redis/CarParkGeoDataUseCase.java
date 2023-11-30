package com.ahmad.carparkapi.persister.redis;

import com.ahmad.carparkapi.controller.CarParkGeoDataConverter;
import com.ahmad.carparkapi.controller.CarParkGeoDataDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class CarParkGeoDataUseCase {
  private final CarParkDataRetriever retriever;

  public Flux<CarParkGeoDataDTO> findWithin(double latitude, double longitude,
      Pageable pageable) {
    return retriever.findWithin(latitude, longitude, pageable)
        .map(CarParkGeoDataConverter::toDTO);
  }
}
