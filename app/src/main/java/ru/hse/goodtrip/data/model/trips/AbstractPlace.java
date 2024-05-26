package ru.hse.goodtrip.data.model.trips;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * AbstractPlace.
 */
@Data
@AllArgsConstructor
public abstract class AbstractPlace implements Serializable {

  private String name;
  private Coordinates coordinates;
}
