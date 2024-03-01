package ru.hse.goodtrip.model;

import java.math.BigDecimal;

public class Coordinates {

  private BigDecimal latitude;
  private BigDecimal longitude;

  public Coordinates(BigDecimal latitude, BigDecimal longitude) {
    this.latitude = latitude;
    this.longitude = longitude;
  }
}
