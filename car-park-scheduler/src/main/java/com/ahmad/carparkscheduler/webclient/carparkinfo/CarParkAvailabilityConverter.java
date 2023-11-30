package com.ahmad.carparkscheduler.webclient.carparkinfo;

import com.ahmad.carparkscheduler.webclient.dto.CarParkAvailabilityDTO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarParkAvailabilityConverter {

  public static Map<String, CarParkAvailabilityInfo> fromDTO(
      final CarParkAvailabilityDTO carParkAvailabilityDTO) {
    Map<String, CarParkAvailabilityInfo> data = new HashMap<>();
    carParkAvailabilityDTO.getItems().forEach(item -> {
      item.getCarParkData().forEach(carParkDataDTO -> {
        CarParkAvailabilityInfo info = new CarParkAvailabilityInfo();
        info.setCarParkNo(carParkDataDTO.getCarParkNumber());
        info.setAvailableLots(carParkDataDTO.getCarParkInfo().stream()
            .mapToInt(value -> Integer.parseInt(value.getLotsAvailable())).sum());
        data.put(carParkDataDTO.getCarParkNumber(), info);
      });
    });
    return data;
  }
}