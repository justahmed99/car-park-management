package com.ahmad.carparkapi.controller;

import com.ahmad.carparkapi.persister.redis.CarParkGeoData;

public class CarParkGeoDataConverter {
  public static CarParkGeoDataDTO toDTO(CarParkGeoData carParkGeoData) {
    return CarParkGeoDataDTO.builder()
        .carParkNo(carParkGeoData.getCarParkNo())
        .address(carParkGeoData.getAddress())
        .latitude(carParkGeoData.getLatitude())
        .longitude(carParkGeoData.getLongitude())
        .lotsAvailable(carParkGeoData.getLotsAvailable())
        .build();
  }
}
