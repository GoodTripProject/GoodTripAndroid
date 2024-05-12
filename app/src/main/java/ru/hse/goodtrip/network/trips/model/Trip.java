package ru.hse.goodtrip.network.trips.model;

import androidx.annotation.Nullable;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Trip network object.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Trip implements Serializable {

  private Integer id;

  private Integer userId;

  private String title;

  private Integer moneyInUsd;

  private String mainPhotoUrl;


  private Date departureDate;

  private Date arrivalDate;

  private Timestamp publicationTimestamp;

  private ru.hse.goodtrip.network.trips.model.TripState state;

  private List<Note> notes;

  private List<CountryVisit> visits;

  /**
   * Creates trip.
   *
   * @param userId        Id of user.
   * @param title         Title of trip.
   * @param moneyInUsd    Budget of trip.
   * @param mainPhotoUrl  Url to main photo.
   * @param departureDate Date of departure.
   * @param arrivalDate   Date of arrival.
   * @param state         State of trip.
   * @param notes         Notes.
   * @param visits        CountryVisits.
   */
  public Trip(Integer userId, String title, Integer moneyInUsd, @Nullable String mainPhotoUrl,
      Date departureDate, Date arrivalDate, ru.hse.goodtrip.network.trips.model.TripState state,
      List<Note> notes,
      List<CountryVisit> visits) {
    this.userId = userId;
    this.title = title;
    this.moneyInUsd = moneyInUsd;
    this.mainPhotoUrl = mainPhotoUrl;
    this.departureDate = departureDate;
    this.arrivalDate = arrivalDate;
    this.state = state;
    this.notes = notes;
    this.visits = visits;
  }
}
