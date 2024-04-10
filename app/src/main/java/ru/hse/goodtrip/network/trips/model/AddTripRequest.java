package ru.hse.goodtrip.network.trips.model;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class AddTripRequest implements Serializable {

  String title;

  Integer moneyInUsd;

  String mainPhotoUrl;

  Date departureDate;

  Date arrivalDate;

  TripState tripState;

  List<AddNoteRequest> notes;

  List<AddCountryRequest> countries;
}