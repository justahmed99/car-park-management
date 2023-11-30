package com.ahmad.carparkscheduler.persister.postgre;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Generated
@Table("public.\"car_park_info\"")
public class CarParkInfoEntity implements Persistable<String> {
  @Id
  private String carParkNo;
  private String address;
  private Double x;
  private Double y;
  private Double latitude;
  private Double longitude;

  @Override
  public String getId() {
    return carParkNo;
  }

  @Override
  public boolean isNew() {
    return true;
  }
}
