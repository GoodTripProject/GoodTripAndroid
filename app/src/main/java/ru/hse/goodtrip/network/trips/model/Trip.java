package ru.hse.goodtrip.network.trips.model;

import androidx.annotation.Nullable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.hse.goodtrip.data.model.trips.TripState;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Trip {

  private Integer id;

  private Integer userId;

  private String title;

  private Integer moneyInUsd;

  private String mainPhotoUrl;


  private Date departureDate;

  private Date arrivalDate;

  private Timestamp publicationTimestamp;

  private TripState state;

  private List<Note> notes;

  private List<CountryVisit> visits;

  public Trip(Integer userId, String title, Integer moneyInUsd, @Nullable String mainPhotoUrl, Date departureDate, Date arrivalDate, TripState state, List<Note> notes, List<CountryVisit> visits) {
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
