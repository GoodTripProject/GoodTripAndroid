package ru.hse.goodtrip.network.trips.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class City {


  private String city;


  private Double longitude;

  private Double latitude;
}
