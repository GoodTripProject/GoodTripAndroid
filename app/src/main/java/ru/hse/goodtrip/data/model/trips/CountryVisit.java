package ru.hse.goodtrip.data.model.trips;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Users visit of country.
 */
@Data
@AllArgsConstructor
public class CountryVisit {

  private Country country;
  private List<City> visitedCities;
}
