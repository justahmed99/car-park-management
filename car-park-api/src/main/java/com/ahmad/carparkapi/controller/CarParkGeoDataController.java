package com.ahmad.carparkapi.controller;

import com.ahmad.carparkapi.persister.redis.CarParkGeoDataUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
@RequestMapping("/carpark")
public class CarParkGeoDataController {

  private final CarParkGeoDataUseCase carParkGeoDataUseCase;

  @GetMapping("/nearest")
  public Flux<CarParkGeoDataDTO> findWithin(
      @RequestParam double latitude,
      @RequestParam double longitude,
      @RequestParam(name = "page", defaultValue = "1") int page,
      @RequestParam(name = "per_page", defaultValue = "10") int perPage) {
    return carParkGeoDataUseCase.findWithin(latitude, longitude,
        PageRequest.of(page <= 0 ? 0 : page - 1, perPage));
  }
}
