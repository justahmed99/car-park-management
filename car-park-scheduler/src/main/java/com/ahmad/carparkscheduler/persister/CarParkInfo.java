package com.ahmad.carparkscheduler.persister;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CarParkInfo {
  private String carParkNo;
  private String address;
  private Double latitude;
  private Double longitude;
  private Integer lotsAvailable;
}
