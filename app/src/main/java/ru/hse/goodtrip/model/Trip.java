package ru.hse.goodtrip.model;

import androidx.annotation.Nullable;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * Users trip.
 */
public class Trip {

  private String name;
  private List<CountryVisit> countries;
  private LocalDate startTripDate;
  private LocalDate endTripDate;
  @Nullable
  private byte[] mainPhoto;
  private int moneyInUsd;
  private Set<ShowPlace> interestingPlacesToVisit;
  private List<Note> notes;

  /**
   * creates trip.
   *
   * @param name                     name of trip.
   * @param countries                countries in trip.
   * @param startTripDate            date of start.
   * @param endTripDate              date of end.
   * @param mainPhoto                photo of trip.
   * @param moneyInUsd               budget of trip.
   * @param interestingPlacesToVisit places in trip.
   */
  public Trip(String name, List<CountryVisit> countries, LocalDate startTripDate,
      LocalDate endTripDate, @Nullable byte[] mainPhoto, int moneyInUsd,
      Set<ShowPlace> interestingPlacesToVisit) {
    this.name = name;
    this.countries = countries;
    this.startTripDate = startTripDate;
    this.endTripDate = endTripDate;
    this.mainPhoto = mainPhoto;
    this.moneyInUsd = moneyInUsd;
    this.interestingPlacesToVisit = interestingPlacesToVisit;
  }
}