package ru.hse.goodtrip.model.trips;

import lombok.Getter;
import lombok.Setter;

/**
 * City.
 */
@Getter
@Setter
public class City extends AbstractPlace {

  Country country;

  public City(String name, Coordinates coordinates, Country country) {
    super(name, coordinates);
    this.country = country;
  }
}
