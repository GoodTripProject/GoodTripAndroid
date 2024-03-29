package ru.hse.goodtrip.data.model.trips;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * AbstractPlace.
 */
@Data
@AllArgsConstructor
public abstract class AbstractPlace {

  private String name;
  private Coordinates coordinates;
}
