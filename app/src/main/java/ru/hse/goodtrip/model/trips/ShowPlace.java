package ru.hse.goodtrip.model.trips;

import java.io.File;
import lombok.Getter;
import lombok.Setter;

/**
 * Place with image.
 */
@Getter
@Setter
public class ShowPlace extends AbstractPlace {

  private File image;

  public ShowPlace(String name, Coordinates coordinates, File image) {
    super(name, coordinates);
    this.image = image;
  }
}
