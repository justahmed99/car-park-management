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

  public static CarParkInfo fromEntity(final CarParkInfoEntity carParkInfoEntity) {
    return CarParkInfo.builder()
        .carParkNo(carParkInfoEntity.getCarParkNo())
        .address(carParkInfoEntity.getAddress())
        .latitude(carParkInfoEntity.getLatitude())
        .longitude(carParkInfoEntity.getLongitude())
        .build();
  }
}
