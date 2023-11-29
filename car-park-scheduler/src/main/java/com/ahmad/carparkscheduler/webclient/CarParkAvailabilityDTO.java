package com.ahmad.carparkscheduler.webclient;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CarParkAvailabilityDTO {
  @JsonProperty("items")
  private List<ItemsDTO> items;
}
