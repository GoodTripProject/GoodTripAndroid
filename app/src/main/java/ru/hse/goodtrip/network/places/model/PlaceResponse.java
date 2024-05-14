package ru.hse.goodtrip.network.places.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response for places API.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaceResponse {
  private String name;

  private double lat;

  private double lng;

  private String icon;

  private int rating;

  private String placeId;
}
