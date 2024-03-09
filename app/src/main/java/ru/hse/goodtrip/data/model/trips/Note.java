package ru.hse.goodtrip.data.model.trips;

import java.io.File;
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
  private File photo;
  private AbstractPlace place;
}
