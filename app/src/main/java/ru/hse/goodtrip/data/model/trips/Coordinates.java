package ru.hse.goodtrip.data.model.trips;

import java.io.Serializable;
import lombok.Data;

/**
 * Coordinates of place.
 */
@Data
public class Coordinates implements Serializable {

  private final double latitude;
  private final double longitude;
}
