package com.ahmad.carparkscheduler;

import static org.mockito.Mockito.when;

import com.ahmad.carparkscheduler.persister.CarParkInfo;
import com.ahmad.carparkscheduler.persister.CarParkService;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import reactor.test.StepVerifier;

@SpringBootTest
class CarParkSchedulerApplicationTests {

  @InjectMocks
  private CarParkService carParkService;

  @Mock
  private ResourceLoader resourceLoader;

  @Mock
  private Resource resource;

  @Test
  void readCarParkInfoFile_ShouldReturnCarParkInfos() throws IOException {
    final String csvData = "ACB,BLK 270/271 ALBERT CENTRE BASEMENT CAR PARK,30314.7936,31490.4942,BASEMENT CAR PARK,ELECTRONIC PARKING,WHOLE DAY,NO,YES,1,1.80,Y";
    final CarParkInfo expectedCarParkInfo = CarParkInfo.builder()
        .carParkNo("ACB")
        .address("BLK 270/271 ALBERT CENTRE BASEMENT CAR PARK")
        .xCoord("30314.7936")
        .yCoord("31490.4942")
        .carParkType("BASEMENT CAR PARK")
        .typeOfParkingSystem("ELECTRONIC PARKING")
        .shortTermPark("WHOLE DAY")
        .freeParking("NO")
        .nightParking("YES")
        .carParkDecks("1")
        .gantryHeight("1.80")
        .carParkBasement("Y")
        .build();

    final InputStream inputStream = new ByteArrayInputStream(csvData.getBytes());
    when(resourceLoader.getResource("classpath:/static/HDBCarparkInformation.csv")).thenReturn(
        resource);
    when(resource.getInputStream()).thenReturn(inputStream);

    StepVerifier.create(carParkService.readCarParkInfoFile())
        .expectNextMatches(
            carParkInfo -> carParkInfo.equals(expectedCarParkInfo))
        .verifyComplete();
  }
}
