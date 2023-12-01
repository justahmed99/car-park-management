package com.ahmad.carparkscheduler.persister.postgre;

import static org.junit.jupiter.api.Assertions.*;

import com.ahmad.carparkscheduler.csv.carparkinfo.CarParkInfoCSV;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class CarParkInfoConverterTest {

  @Test
  void fromCSVToEntity() {
    final CarParkInfoCSV carParkInfoCSV = CarParkInfoCSV.builder()
        .carParkNo("CP001")
        .address("1234 Street Name")
        .xCoord(1.234)
        .yCoord(5.678)
        .build();

    StepVerifier.create(CarParkInfoConverter.fromCSVToEntity(carParkInfoCSV))
        .assertNext(carParkInfoEntity -> {
          assertEquals("CP001", carParkInfoEntity.getCarParkNo());
          assertEquals("1234 Street Name", carParkInfoEntity.getAddress());
          assertEquals(1.234, carParkInfoEntity.getX());
          assertEquals(5.678, carParkInfoEntity.getY());
        })
        .verifyComplete();
  }

  @Test
  void fromEntity() {
    final CarParkInfoEntity carParkInfoEntity = CarParkInfoEntity.builder()
        .carParkNo("CP001")
        .address("1234 Street Name")
        .latitude(1.234)
        .longitude(5.678)
        .build();

    CarParkInfo result = CarParkInfoConverter.fromEntity(carParkInfoEntity);

    assertEquals("CP001", result.getCarParkNo());
    assertEquals("1234 Street Name", result.getAddress());
    assertEquals(1.234, result.getLatitude());
    assertEquals(5.678, result.getLongitude());
  }
}