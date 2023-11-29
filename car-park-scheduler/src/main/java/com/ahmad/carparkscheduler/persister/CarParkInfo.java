package com.ahmad.carparkscheduler.persister;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CarParkInfo {
  private final String carParkNo;
  private final String address;
  private final String xCoord;
  private final String yCoord;
  private final String carParkType;
  private final String typeOfParkingSystem;
  private final String shortTermPark;
  private final String freeParking;
  private final String nightParking;
  private final String carParkDecks;
  private final String gantryHeight;
  private final String carParkBasement;
}
