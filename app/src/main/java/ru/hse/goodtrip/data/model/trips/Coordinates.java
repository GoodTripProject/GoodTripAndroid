package ru.hse.goodtrip.data.model.trips;

import lombok.Data;

/**
 * Coordinates of place.
 */
@Data
public class Coordinates {

  private final double latitude;
  private final double longitude;
}
