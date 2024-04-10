package ru.hse.goodtrip.network.trips.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityVisit {

  private Integer id;


  private String city;


  private Point point;

  private Integer countryVisitId;
}
