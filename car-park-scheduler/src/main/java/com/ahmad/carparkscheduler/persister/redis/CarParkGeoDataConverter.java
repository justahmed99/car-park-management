package com.ahmad.carparkscheduler.persister.redis;

import com.ahmad.carparkscheduler.persister.postgre.CarParkInfo;

public class CarParkGeoDataConverter {

  public static CarParkGeoData fromScheduler(final CarParkInfo carParkInfo) {
    return CarParkGeoData.builder()
        .carParkNo(carParkInfo.getCarParkNo())
        .latitude(carParkInfo.getLatitude())
        .longitude(carParkInfo.getLongitude())
        .address(carParkInfo.getAddress())
        .lotsAvailable(carParkInfo.getLotsAvailable())
        .build();
  }

}
