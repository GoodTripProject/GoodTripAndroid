package ru.hse.goodtrip.model.trips;

import androidx.annotation.Nullable;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import lombok.Data;
import ru.hse.goodtrip.model.User;

/**
 * Users trip.
 */
@Data
public class Trip {

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
  private LocalDate publicationTime;

  /**
   * creates trip.
   *
   * @param name                     name of trip.
   * @param countries                countries in trip.
   * @param startTripDate            date of start.
   * @param endTripDate              date of end.
   * @param mainPhotoUrl             photo of trip.
   * @param moneyInUsd               budget of trip.
   * @param interestingPlacesToVisit places in trip.
   * @param user                     user trip associated with.
   */
  public Trip(String name, List<CountryVisit> countries, LocalDate startTripDate,
      LocalDate endTripDate, @Nullable String mainPhotoUrl, int moneyInUsd,
      Set<ShowPlace> interestingPlacesToVisit, User user) {
    this.title = name;
    this.countries = countries;
    this.startTripDate = startTripDate;
    this.endTripDate = endTripDate;
    this.mainPhotoUrl = mainPhotoUrl;
    this.moneyInUsd = moneyInUsd;
    this.interestingPlacesToVisit = interestingPlacesToVisit;
    this.user = user;
  }
}