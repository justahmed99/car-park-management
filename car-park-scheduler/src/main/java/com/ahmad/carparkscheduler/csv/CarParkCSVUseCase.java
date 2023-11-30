package com.ahmad.carparkscheduler.csv;

import com.ahmad.carparkscheduler.csv.carparkinfo.CarParkCSVConverter;
import com.ahmad.carparkscheduler.csv.carparkinfo.CarParkInfoCSV;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CarParkCSVUseCase {

  private final ResourceLoader resourceLoader;

  public List<CarParkInfoCSV> readCarParkInfoFile() {
    final List<CarParkInfoCSV> carParkInfoCSVS = new ArrayList<>();
    final Resource resource = resourceLoader.getResource("classpath:/static/HDBCarparkInformation.csv");
    try (Reader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
      CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());
      for (CSVRecord record: csvParser) {
        carParkInfoCSVS.add(CarParkCSVConverter.convertStringLineToCarParkInfo(record));
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return carParkInfoCSVS;
  }

  public Integer getFileLength() {
    final Resource resource = resourceLoader.getResource("classpath:/static/HDBCarparkInformation.csv");

    try (Reader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
      CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());
      Iterable<CSVRecord> records = csvParser.getRecords();

      return (int) StreamSupport.stream(records.spliterator(), false).count();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
