package com.ahmad.carparkscheduler.webclient;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CarParkAvailabilityInfo {
  private String carParkNo;
  private Integer availableLots;
}
