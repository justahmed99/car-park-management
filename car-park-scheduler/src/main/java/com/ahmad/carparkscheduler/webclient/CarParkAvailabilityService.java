package com.ahmad.carparkscheduler.webclient;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
public class CarParkAvailabilityService {

  public Flux<List<CarParkAvailabilityInfo>> getCarParkAvailabilityInfo() {
    final WebClient webClient = WebClient.builder()
        .baseUrl("https://api.data.gov.sg/v1")
        .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024))
        .build();
    return webClient.get()
        .uri("/transport/carpark-availability")
        .retrieve()
        .bodyToFlux(CarParkAvailabilityDTO.class)
        .mapNotNull(carParkAvailabilityDTO -> CarParkAvailabilityConverter.fromDTO(carParkAvailabilityDTO));
  }
}
