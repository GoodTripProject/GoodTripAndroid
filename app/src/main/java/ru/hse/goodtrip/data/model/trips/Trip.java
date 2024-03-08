package ru.hse.goodtrip.data.model.trips;

import androidx.annotation.Nullable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import lombok.Data;
import ru.hse.goodtrip.data.model.User;

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
  private LocalDate timeOfPublication = LocalDate.now(); // TODO
  private TripState tripState;

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
    this.tripState = TripState.PLANNED;
  }

  /**
   * Return LocalDate in provided format.
   *
   * @param date local date to format
   * @return date in dd MM yyyy format
   */
  public static String getDateFormatted(LocalDate date, String format) {
    return date.format(DateTimeFormatter.ofPattern(format));
  }

  /**
   * Return trip duration in "start - end" format.
   *
   * @return trip duration in dd MM yyyy format
   */
  public String getDuration(String format) {
    return getDateFormatted(startTripDate, format) + " - " + getDateFormatted(endTripDate, format);
  }

  private enum TripState {
    PLANNED, IN_PROCESS, PUBLISHED
  }
}