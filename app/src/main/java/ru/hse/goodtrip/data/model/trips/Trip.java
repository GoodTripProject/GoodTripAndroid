package ru.hse.goodtrip.data.model.trips;

import androidx.annotation.Nullable;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import lombok.Data;
import ru.hse.goodtrip.data.model.User;

/**
 * Users trip.
 */
@Data
public class Trip implements Serializable {

  private String title;
  private List<CountryVisit> countries;
  private LocalDate startTripDate;
  private LocalDate endTripDate;
  @Nullable
  private String mainPhotoUrl;
  private int moneyInUsd;
  private Set<ShowPlace> interestingPlacesToVisit;
  private List<Note> notes;
  private User user;
  private LocalDate timeOfPublication = LocalDate.now(); // TODO
  private TripState tripState;
  private long tripId;

  /**
   * creates trip.
   *
   * @param title                    name of trip.
   * @param countries                countries in trip.
   * @param startTripDate            date of start.
   * @param endTripDate              date of end.
   * @param mainPhotoUrl             photo of trip.
   * @param moneyInUsd               budget of trip.
   * @param interestingPlacesToVisit places in trip.
   * @param user                     user trip associated with.
   */
  public Trip(String title, List<CountryVisit> countries, LocalDate startTripDate,
      LocalDate endTripDate, @Nullable String mainPhotoUrl, int moneyInUsd,
      Set<ShowPlace> interestingPlacesToVisit, User user) {
    this.title = title;
    this.countries = countries;
    this.startTripDate = startTripDate;
    this.endTripDate = endTripDate;
    this.mainPhotoUrl = mainPhotoUrl;
    this.moneyInUsd = moneyInUsd;
    this.interestingPlacesToVisit = interestingPlacesToVisit;
    this.user = user;
    this.tripState = TripState.IN_PLANNING;
  }

  private enum TripState {
    IN_PLANNING, IN_PROGRESS, PUBLISHED
  }
}