package ru.hse.goodtrip.data.model.trips;

import java.io.Serializable;

/**
 * Country.
 */
public class Country extends AbstractPlace implements Serializable {

  public Country(String name, Coordinates coordinates) {
    super(name, coordinates);
  }
}
