package ru.hse.goodtrip.data.model.tripRequests.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AddNoteRequest {

    String title;

    String photoUrl;

    String googlePlaceId;

    Integer tripId;

}
