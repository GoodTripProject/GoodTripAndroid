package ru.hse.goodtrip.data.model.trips;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * City.
 */
@Getter
@Setter
public class City extends AbstractPlace implements Serializable {

  Country country;

  public City(String name, Coordinates coordinates, Country country) {
    super(name, coordinates);
    this.country = country;
  }
}
