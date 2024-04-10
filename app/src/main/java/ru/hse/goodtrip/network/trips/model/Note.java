package ru.hse.goodtrip.network.trips.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Note {

  private Integer id;

  private String title;

  private String photoUrl;

  private String googlePlaceId;

  private String text;

  private Integer tripId;

}
