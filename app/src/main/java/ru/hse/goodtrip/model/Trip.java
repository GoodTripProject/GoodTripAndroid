package ru.hse.goodtrip.model;

import androidx.annotation.Nullable;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class Trip {

  private String name;
  private List<CountryVisit> countries;
  private LocalDate startTripDate;
  private LocalDate endTripDate;
  @Nullable
  private byte[] mainPhoto;
  private int moneyInUSD;
  private Set<ShowPlace> interestingPlacesToVisit;
  private List<Note> notes;

  public Trip(String name, List<CountryVisit> countries, LocalDate startTripDate,
      LocalDate endTripDate, @Nullable byte[] mainPhoto, int moneyInUSD,
      Set<ShowPlace> interestingPlacesToVisit) {
    this.name = name;
    this.countries = countries;
    this.startTripDate = startTripDate;
    this.endTripDate = endTripDate;
    this.mainPhoto = mainPhoto;
    this.moneyInUSD = moneyInUSD;
    this.interestingPlacesToVisit = interestingPlacesToVisit;
    this.notes = notes;
  }
}
