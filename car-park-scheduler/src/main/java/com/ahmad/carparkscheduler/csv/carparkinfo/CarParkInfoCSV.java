package com.ahmad.carparkscheduler.csv.carparkinfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CarParkInfoCSV {
  private final String carParkNo;
  private final String address;
  private final Double xCoord;
  private final Double yCoord;
  private final String carParkType;
  private final String typeOfParkingSystem;
  private final String shortTermPark;
  private final String freeParking;
  private final String nightParking;
  private final String carParkDecks;
  private final String gantryHeight;
  private final String carParkBasement;
}
