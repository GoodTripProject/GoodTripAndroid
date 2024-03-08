package ru.hse.goodtrip.data.model.trips;

import java.math.BigDecimal;
import lombok.Data;

/**
 * Coordinates of place.
 */
@Data
public class Coordinates {

  private final BigDecimal latitude;
  private final BigDecimal longitude;
}
