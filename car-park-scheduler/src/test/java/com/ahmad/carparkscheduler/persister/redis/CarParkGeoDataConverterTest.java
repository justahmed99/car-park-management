package com.ahmad.carparkscheduler.persister.redis;

import static org.junit.jupiter.api.Assertions.*;

import com.ahmad.carparkscheduler.persister.postgre.CarParkInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CarParkGeoDataConverterTest {

  @Test
  void fromScheduler() {
    CarParkInfo carParkInfo = CarParkInfo.builder()
        .carParkNo("CP001")
        .latitude(1.23456)
        .longitude(2.34567)
        .address("1234 CarPark Street")
        .lotsAvailable(100)
        .build();

    CarParkGeoData result = CarParkGeoDataConverter.fromScheduler(carParkInfo);

    assertEquals("CP001", result.getCarParkNo());
    assertEquals(1.23456, result.getLatitude());
    assertEquals(2.34567, result.getLongitude());
    assertEquals("1234 CarPark Street", result.getAddress());
    assertEquals(100, result.getLotsAvailable());
  }
}