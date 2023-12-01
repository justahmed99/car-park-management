package com.ahmad.carparkscheduler.webclient.carparkinfo;

import static org.junit.jupiter.api.Assertions.*;

import com.ahmad.carparkscheduler.webclient.availability.CarParkAvailabilityDTO;
import com.ahmad.carparkscheduler.webclient.availability.CarParkDataDTO;
import com.ahmad.carparkscheduler.webclient.availability.CarParkInfoDTO;
import com.ahmad.carparkscheduler.webclient.availability.ItemsDTO;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CarParkAvailabilityConverterTest {

  @Test
  void fromDTO() {
    final CarParkInfoDTO carParkInfoDTO1 = new CarParkInfoDTO();
    carParkInfoDTO1.setLotsAvailable("100");
    carParkInfoDTO1.setLotType("C");
    carParkInfoDTO1.setTotalLots("200");

    final CarParkInfoDTO carParkInfoDTO2 = new CarParkInfoDTO();
    carParkInfoDTO2.setLotsAvailable("150");
    carParkInfoDTO2.setLotType("C");
    carParkInfoDTO2.setTotalLots("300");

    final CarParkDataDTO carParkDataDTO = new CarParkDataDTO();
    carParkDataDTO.setCarParkNumber("CP001");
    carParkDataDTO.setUpdateDatetime("2023-01-01T12:00:00");
    carParkDataDTO.setCarParkInfo(List.of(carParkInfoDTO1, carParkInfoDTO2));

    final ItemsDTO itemsDTO = new ItemsDTO();
    itemsDTO.setTimestamp("2023-01-01T12:00:00");
    itemsDTO.setCarParkData(List.of(carParkDataDTO));

    final CarParkAvailabilityDTO carParkAvailabilityDTO = new CarParkAvailabilityDTO();
    carParkAvailabilityDTO.setItems(List.of(itemsDTO));

    Map<String, CarParkAvailabilityInfo> result = CarParkAvailabilityConverter.fromDTO(carParkAvailabilityDTO);

    assertEquals(1, result.size());
    CarParkAvailabilityInfo info = result.get("CP001");
    assertEquals("CP001", info.getCarParkNo());
    assertEquals(250, info.getAvailableLots());
  }
}