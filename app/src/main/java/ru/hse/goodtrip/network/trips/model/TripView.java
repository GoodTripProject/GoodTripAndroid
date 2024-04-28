package ru.hse.goodtrip.network.trips.model;

import java.sql.Date;
import java.sql.Timestamp;
import lombok.Data;
import ru.hse.goodtrip.data.model.trips.TripState;

@Data
public class TripView {

  private Integer id;

  private Integer userId;

  private String title;

  private Integer moneyInUsd;

  private String mainPhotoUrl;

  private Date departureDate;


  private Date arrivalDate;


  private Timestamp publicationTimestamp;

  private TripState state;

  public TripView(Trip trip) {
    id = trip.getId();
    userId = trip.getUserId();
    title = trip.getTitle();
    moneyInUsd = trip.getMoneyInUsd();
    mainPhotoUrl = trip.getMainPhotoUrl();
    departureDate = trip.getDepartureDate();
    arrivalDate = trip.getArrivalDate();
    publicationTimestamp = trip.getPublicationTimestamp();
    state = trip.getState();
  }

}
