package com.ahmad.carparkapi.controller;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CarParkGeoDataDTO {
  private String carParkNo;
  private String address;
  private Double latitude;
  private Double longitude;
  private Integer lotsAvailable;
}
