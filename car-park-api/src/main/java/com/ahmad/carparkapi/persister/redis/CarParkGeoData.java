package com.ahmad.carparkapi.persister.redis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CarParkGeoData {
  private String carParkNo;
  private String address;
  private Double latitude;
  private Double longitude;
  private Integer lotsAvailable;
}
