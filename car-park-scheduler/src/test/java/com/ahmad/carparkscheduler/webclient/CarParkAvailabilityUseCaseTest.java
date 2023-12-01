package com.ahmad.carparkscheduler.webclient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import com.ahmad.carparkscheduler.webclient.carparkinfo.CarParkAvailabilityInfo;
import com.ahmad.carparkscheduler.webclient.coordinate.SVY21Coordinate;
import java.io.IOException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class CarParkAvailabilityUseCaseTest {

  private MockWebServer mockWebServer;
  private CarParkAvailabilityUseCase carParkAvailabilityUseCase;

  @BeforeEach
  void setUp() throws IOException {
    mockWebServer = new MockWebServer();
    mockWebServer.start();
    carParkAvailabilityUseCase = new CarParkAvailabilityUseCase();
    setField(carParkAvailabilityUseCase, "sgParkUrl", mockWebServer.url("/").toString());
    setField(carParkAvailabilityUseCase, "oneMapSgUrl", mockWebServer.url("/").toString());
    setField(carParkAvailabilityUseCase, "oneMapSgToken", "mocked-token");
  }

  @AfterEach
  void tearDown() throws IOException {
    mockWebServer.shutdown();
  }

  @Test
  void getCarParkAvailabilityInfo() {
    String mockResponseJson = "{\"items\":[{\"carpark_data\":[{\"carpark_number\":\"HE12\",\"carpark_info\":[{\"total_lots\":\"105\",\"lot_type\":\"C\",\"lots_available\":\"0\"}],\"update_datetime\":\"2023-12-01T09:05:36\"},{\"carpark_number\":\"HLM\",\"carpark_info\":[{\"total_lots\":\"583\",\"lot_type\":\"C\",\"lots_available\":\"292\"}],\"update_datetime\":\"2023-12-01T09:05:29\"}]}]}";

    String lotsJson = "{\"SD2\":{\"carParkNo\":\"SD2\",\"availableLots\":1},\"SD1\":{\"carParkNo\":\"SD1\",\"availableLots\":67}}";
    mockWebServer.enqueue(new MockResponse().setBody(mockResponseJson).addHeader("Content-Type", "application/json"));

    StepVerifier.create(carParkAvailabilityUseCase.getCarParkAvailabilityInfo())
        .expectNextMatches(availabilityMap -> {
          assertTrue(availabilityMap.containsKey("HE12"));
          assertTrue(availabilityMap.containsKey("HLM"));

          CarParkAvailabilityInfo sd2Info = availabilityMap.get("HE12");
          assertNotNull(sd2Info);
          assertEquals("HE12", sd2Info.getCarParkNo());
          assertEquals(0, sd2Info.getAvailableLots());

          CarParkAvailabilityInfo sd1Info = availabilityMap.get("HLM");
          assertNotNull(sd1Info);
          assertEquals("HLM", sd1Info.getCarParkNo());
          assertEquals(292, sd1Info.getAvailableLots());

          return true;
        })
        .verifyComplete();
  }

  @Test
  void getWgs84Coordinate() {
    String coordinateJson = "{\"latitude\":1.2345,\"longitude\":3.4567}";
    mockWebServer.enqueue(new MockResponse().setBody(coordinateJson).addHeader("Content-Type", "application/json"));

    SVY21Coordinate svy21Coordinate = SVY21Coordinate.builder().x(12345.0).y(67890.0).build();

    StepVerifier.create(carParkAvailabilityUseCase.getWgs84Coordinate(svy21Coordinate))
        .expectNextMatches(coordinate -> {
          assertEquals(1.2345, coordinate.getLatitude(), 0.0001);
          assertEquals(3.4567, coordinate.getLongitude(), 0.0001);
          return true;
        })
        .verifyComplete();
  }
}