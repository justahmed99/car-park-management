package com.ahmad.carparkscheduler.webclient.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CarParkInfoDTO {
  @JsonProperty("total_lots")
  private String totalLots;
  @JsonProperty("lot_type")
  private String lotType;
  @JsonProperty("lots_available")
  private String lotsAvailable;
}
