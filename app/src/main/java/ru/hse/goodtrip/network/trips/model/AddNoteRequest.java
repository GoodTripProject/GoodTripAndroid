package ru.hse.goodtrip.network.trips.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AddNoteRequest {

  String title;

  String photoUrl;

  String googlePlaceId;

  Integer tripId;

}
