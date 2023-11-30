package com.ahmad.carparkscheduler.webclient;

import com.ahmad.carparkscheduler.csv.CarParkCSVService;
import com.ahmad.carparkscheduler.csv.carparkinfo.CarParkInfoCSV;
import com.ahmad.carparkscheduler.webclient.carparkinfo.CarParkAvailabilityInfo;
import com.ahmad.carparkscheduler.webclient.coordinate.SVY21Coordinate;
import com.ahmad.carparkscheduler.webclient.coordinate.WGS84Coordinate;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class TestController {

  private final CarParkAvailabilityService service;
  private final CarParkCSVService carParkCSVService;

  @GetMapping("/car-park")
  public Mono<Map<String, CarParkAvailabilityInfo>> getCarParks() {
    return service.getCarParkAvailabilityInfo();
  }

  @GetMapping("/coordinate")
  public Mono<WGS84Coordinate> getCoordinate(@RequestParam(name = "x") final Double x,
      @RequestParam(name = "y") final Double y) {
    return service.getWgs84Coordinate(SVY21Coordinate.builder().x(x).y(y).build());
  }

  @GetMapping("/car-park-csv")
  public List<CarParkInfoCSV> getCarParksCSV() {
    return carParkCSVService.readCarParkInfoFile();
  }
}
