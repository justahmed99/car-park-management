package com.ahmad.carparkscheduler.webclient;

import com.ahmad.carparkscheduler.webclient.carparkinfo.CarParkAvailabilityConverter;
import com.ahmad.carparkscheduler.webclient.carparkinfo.CarParkAvailabilityInfo;
import com.ahmad.carparkscheduler.webclient.coordinate.SVY21Coordinate;
import com.ahmad.carparkscheduler.webclient.coordinate.WGS84Coordinate;
import com.ahmad.carparkscheduler.webclient.dto.CarParkAvailabilityDTO;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CarParkAvailabilityService {

  @Value("${sg-park.url}")
  private String sgParkUrl;

  @Value("${one-map-sg.url}")
  private String oneMapSgUrl;

  @Value("${one-map-sg.token}")
  private String oneMapSgToken;

  public Mono<Map<String, CarParkAvailabilityInfo>> getCarParkAvailabilityInfo() {
    final WebClient webClient = WebClient.builder()
        .baseUrl(sgParkUrl)
        .codecs(configurer -> configurer.defaultCodecs()
            .maxInMemorySize(16 * 1024 * 1024))
        .build();
    return webClient.get()
        .retrieve()
        .bodyToMono(CarParkAvailabilityDTO.class)
        .mapNotNull(CarParkAvailabilityConverter::fromDTO);
  }

  public Mono<WGS84Coordinate> getWgs84Coordinate(final SVY21Coordinate SVY21Coordinate) {
    final WebClient webClient = WebClient.builder()
        .baseUrl(oneMapSgUrl)
        .build();
    return webClient.get()
        .uri("?X=" + SVY21Coordinate.getX() + "&Y=" + SVY21Coordinate.getY())
        .headers(httpHeaders -> httpHeaders.setBearerAuth(oneMapSgToken))
        .retrieve()
        .bodyToMono(WGS84Coordinate.class);
  }
}
