package com.ahmad.carparkscheduler.webclient;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemsDTO {
  @JsonProperty("timestamp")
  private String timestamp;
  @JsonProperty("carpark_data")
  private List<CarParkDataDTO> carParkData;
}
