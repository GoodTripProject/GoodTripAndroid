package ru.hse.goodtrip.network.trips.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountryVisit {

  private Integer id;

  private String country;


  private List<CityVisit> cities;

  private Integer tripId;

}
