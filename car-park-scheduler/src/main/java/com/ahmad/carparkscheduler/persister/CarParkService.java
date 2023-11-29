package com.ahmad.carparkscheduler.persister;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class CarParkService {

  private final ResourceLoader resourceLoader;

  public Flux<CarParkInfo> readCarParkInfoFile() {
    List<CarParkInfo> carParkInfos = new ArrayList<>();
    final Resource resource = resourceLoader.getResource("classpath:/static/HDBCarparkInformation.csv");
    try (Reader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
      CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);
      for (CSVRecord record: csvParser) {
        carParkInfos.add(CarParkConverter.convertStringLineToCarParkInfo(record));
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return Flux.fromIterable(carParkInfos);
  }
}
