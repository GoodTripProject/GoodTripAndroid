package ru.hse.goodtrip.model;

import java.io.File;

/**
 * Place with image.
 */
public class ShowPlace extends AbstractPlace {

  private File image;

  public ShowPlace(String name, Coordinates coordinates, File image) {
    super(name, coordinates);
    this.image = image;
  }
}
