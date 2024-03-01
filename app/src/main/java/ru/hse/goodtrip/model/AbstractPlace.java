package ru.hse.goodtrip.model;

public abstract class AbstractPlace {

  private String name;
  private Coordinates coordinates;

  public AbstractPlace(String name, Coordinates coordinates) {
    this.name = name;
    this.coordinates = coordinates;
  }
}
