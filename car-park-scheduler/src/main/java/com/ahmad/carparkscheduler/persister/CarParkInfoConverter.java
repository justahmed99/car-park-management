package com.ahmad.carparkscheduler.persister;

import com.ahmad.carparkscheduler.csv.carparkinfo.CarParkInfoCSV;
import reactor.core.publisher.Mono;

public class CarParkInfoConverter {

  public static Mono<CarParkInfoEntity> fromCSVToEntity(final CarParkInfoCSV carParkInfoCSV) {
    return Mono.just(CarParkInfoEntity.builder()
        .carParkNo(carParkInfoCSV.getCarParkNo())
        .address(carParkInfoCSV.getAddress())
        .x(carParkInfoCSV.getXCoord())
        .y(carParkInfoCSV.getYCoord())
        .build());
  }
}
