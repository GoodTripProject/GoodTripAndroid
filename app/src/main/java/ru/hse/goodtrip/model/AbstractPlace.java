package ru.hse.goodtrip.model;

/**
 * AbstractPlace.
 */
public abstract class AbstractPlace {

  private final String name;
  private final Coordinates coordinates;

  public AbstractPlace(String name, Coordinates coordinates) {
    this.name = name;
    this.coordinates = coordinates;
  }
}
