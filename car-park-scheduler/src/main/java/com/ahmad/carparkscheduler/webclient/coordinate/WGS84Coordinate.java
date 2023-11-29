package com.ahmad.carparkscheduler.webclient.coordinate;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class WGS84Coordinate {
  private Double latitude;
  private Double longitude;
}
