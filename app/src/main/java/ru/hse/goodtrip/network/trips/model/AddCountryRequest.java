package ru.hse.goodtrip.network.trips.model;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddCountryRequest implements Serializable {

  String country;

  List<City> cities;
}
