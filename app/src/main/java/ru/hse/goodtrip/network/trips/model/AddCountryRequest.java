package ru.hse.goodtrip.network.trips.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class AddCountryRequest {

  String country;

  List<CityVisit> cities;
}
