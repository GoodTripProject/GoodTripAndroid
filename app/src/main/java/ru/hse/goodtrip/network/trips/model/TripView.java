package ru.hse.goodtrip.network.trips.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.hse.goodtrip.data.model.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TripView {

  private Integer id;

  private String displayName;

  private String userMainPhotoUrl;

  private String title;

  private Integer moneyInUsd;

  private String mainPhotoUrl;

  private Date departureDate;

  private Date arrivalDate;

  private Timestamp publicationTimestamp;

  private ru.hse.goodtrip.network.trips.model.TripState state;

  private List<CountryVisit> visits;

  public TripView(Trip trip, User user) {
    id = trip.getId();
    displayName = user.getDisplayName();
    userMainPhotoUrl = user.getMainPhotoUrl().toString();
    title = trip.getTitle();
    visits = trip.getVisits();
    moneyInUsd = trip.getMoneyInUsd();
    mainPhotoUrl = trip.getMainPhotoUrl();
    departureDate = trip.getDepartureDate();
    arrivalDate = trip.getArrivalDate();
    publicationTimestamp = trip.getPublicationTimestamp();
    state = trip.getState();
  }

}