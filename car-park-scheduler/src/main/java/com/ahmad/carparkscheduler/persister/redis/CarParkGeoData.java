package com.ahmad.carparkscheduler.persister.redis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CarParkGeoData {
  private String carParkNo;
  private String address;
  private Double latitude;
  private Double longitude;
  private Integer lotsAvailable;
}
