package ru.hse.goodtrip.data.model.tripRequests.model;

import java.sql.Date;
import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AddTripRequest {

    String title;

    Integer moneyInUsd;

    String mainPhotoUrl;

    Date departureDate;

    Date arrivalDate;

    TripState tripState;

    List<AddNoteRequest> notes;

    List<AddCountryRequest> countries;
}