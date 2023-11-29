package com.ahmad.carparkscheduler.csv.carparkinfo;

import org.apache.commons.csv.CSVRecord;

public class CarParkCSVConverter {
  public static CarParkInfoCSV convertStringLineToCarParkInfo(final CSVRecord record) {
    return CarParkInfoCSV.builder()
        .carParkNo(record.get(0))
        .address(record.get(1))
        .xCoord(Double.parseDouble(record.get(2)))
        .yCoord(Double.parseDouble(record.get(3)))
        .carParkType(record.get(4))
        .typeOfParkingSystem(record.get(5))
        .shortTermPark(record.get(6))
        .freeParking(record.get(7))
        .nightParking(record.get(8))
        .carParkDecks(record.get(9))
        .gantryHeight(record.get(10))
        .carParkBasement(record.get(11))
        .build();
  }
}
