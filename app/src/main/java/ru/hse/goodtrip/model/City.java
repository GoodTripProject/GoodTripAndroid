package ru.hse.goodtrip.model;

public class City extends AbstractPlace {

  Country country;

  public City(String name, Coordinates coordinates, Country country) {
    super(name, coordinates);
    this.country = country;
  }
}
