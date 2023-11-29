package com.ahmad.carparkscheduler.webclient.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CarParkDataDTO {
  @JsonProperty("carpark_info")
  private List<CarParkInfoDTO> carParkInfo;
  @JsonProperty("carpark_number")
  private String carParkNumber;
  @JsonProperty("update_datetime")
  private String updateDatetime;
}
