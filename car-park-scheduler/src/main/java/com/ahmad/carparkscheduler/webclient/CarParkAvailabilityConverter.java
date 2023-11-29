package com.ahmad.carparkscheduler.webclient;

import java.util.ArrayList;
import java.util.List;

public class CarParkAvailabilityConverter {

  static List<CarParkAvailabilityInfo> fromDTO(
      final CarParkAvailabilityDTO carParkAvailabilityDTO) {
    List<CarParkAvailabilityInfo> data = new ArrayList<>();
    carParkAvailabilityDTO.getItems().forEach(item -> {
      CarParkAvailabilityInfo info = new CarParkAvailabilityInfo();
      item.getCarParkData().forEach(carParkDataDTO -> {
        info.setCarParkNo(carParkDataDTO.getCarParkNumber());
        info.setAvailableLots(carParkDataDTO.getCarParkInfo().stream()
            .mapToInt(value -> Integer.parseInt(value.getTotalLots())).sum());
        data.add(info);
      });
    });
    return data;
  }
}