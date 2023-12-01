package com.ahmad.carparkscheduler;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.ahmad.carparkscheduler.csv.carparkinfo.CarParkInfoCSV;
import com.ahmad.carparkscheduler.csv.CarParkCSVUseCase;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

@SpringBootTest
class CarParkSchedulerApplicationTests {
  @Test
  void contextLoads() {
    CarParkSchedulerApplication.main(new String[]{});
    Assertions.assertTrue(true);
  }
}
