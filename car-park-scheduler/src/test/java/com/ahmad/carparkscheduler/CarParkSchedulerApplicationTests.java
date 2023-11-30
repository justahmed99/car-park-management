package com.ahmad.carparkscheduler;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.ahmad.carparkscheduler.csv.carparkinfo.CarParkInfoCSV;
import com.ahmad.carparkscheduler.csv.CarParkCSVUseCase;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

@SpringBootTest
class CarParkSchedulerApplicationTests {

  @InjectMocks
  private CarParkCSVUseCase carParkCSVUseCase;

  @Mock
  private ResourceLoader resourceLoader;

  @Mock
  private Resource resource;

  @Test
  void readCarParkInfoFile_ShouldReturnCarParkInfos() throws IOException {
    final String csvData = "car_park_no,address,x_coord,y_coord,car_park_type,type_of_parking_system,short_term_parking,free_parking,night_parking,car_park_decks,gantry_height,car_park_basement\n"
        + "ACB,BLK 270/271 ALBERT CENTRE BASEMENT CAR PARK,30314.7936,31490.4942,BASEMENT CAR PARK,ELECTRONIC PARKING,WHOLE DAY,NO,YES,1,1.80,Y";
    final List<CarParkInfoCSV> expectedCarParkInfoCSV = List.of(
        CarParkInfoCSV.builder()
            .carParkNo("ACB")
            .address("BLK 270/271 ALBERT CENTRE BASEMENT CAR PARK")
            .xCoord(30314.7936)
            .yCoord(31490.4942)
            .carParkType("BASEMENT CAR PARK")
            .typeOfParkingSystem("ELECTRONIC PARKING")
            .shortTermPark("WHOLE DAY")
            .freeParking("NO")
            .nightParking("YES")
            .carParkDecks("1")
            .gantryHeight("1.80")
            .carParkBasement("Y")
            .build()
    );

    final InputStream inputStream = new ByteArrayInputStream(csvData.getBytes());
    when(resourceLoader.getResource("classpath:/static/HDBCarparkInformation.csv")).thenReturn(
        resource);
    when(resource.getInputStream()).thenReturn(inputStream);

    assertThat(carParkCSVUseCase.readCarParkInfoFile()).isEqualTo(expectedCarParkInfoCSV);
  }

  @Test
  void getFileLength() throws IOException {
    final String csvData = "car_park_no,address,x_coord,y_coord,car_park_type,type_of_parking_system,short_term_parking,free_parking,night_parking,car_park_decks,gantry_height,car_park_basement\n"
        + "ACB,BLK 270/271 ALBERT CENTRE BASEMENT CAR PARK,30314.7936,31490.4942,BASEMENT CAR PARK,ELECTRONIC PARKING,WHOLE DAY,NO,YES,1,1.80,Y\n"
        + "ACM,BLK 98A ALJUNIED CRESCENT,33758.4143,33695.5198,MULTI-STOREY CAR PARK,ELECTRONIC PARKING,WHOLE DAY,SUN & PH FR 7AM-10.30PM,YES,5,2.10,N\n"
        + "AH1,BLK 101 JALAN DUSUN,29257.7203,34500.3599,SURFACE CAR PARK,ELECTRONIC PARKING,WHOLE DAY,SUN & PH FR 7AM-10.30PM,YES,0,0.00,N\n"
        + "AK19,BLOCK 253 ANG MO KIO STREET 21,28185.4359,39012.6664,SURFACE CAR PARK,COUPON PARKING,7AM-7PM,NO,NO,0,0.00,N";
    final InputStream inputStream = new ByteArrayInputStream(csvData.getBytes());
    when(resourceLoader.getResource("classpath:/static/HDBCarparkInformation.csv")).thenReturn(
        resource);
    when(resource.getInputStream()).thenReturn(inputStream);

    assertThat(carParkCSVUseCase.getFileLength()).isEqualTo(4);
  }
}
