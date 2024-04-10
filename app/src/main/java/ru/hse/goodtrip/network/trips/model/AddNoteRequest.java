package ru.hse.goodtrip.network.trips.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddNoteRequest implements Serializable {

  String title;

  String photoUrl;

  String googlePlaceId;

  Integer tripId;

}
