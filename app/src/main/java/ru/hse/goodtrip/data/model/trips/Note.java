package ru.hse.goodtrip.data.model.trips;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Users note about place.
 */
@Data
@AllArgsConstructor
public class Note {

  private String headline;
  private String note;
  private String photoUrl;
  private AbstractPlace place;
}
