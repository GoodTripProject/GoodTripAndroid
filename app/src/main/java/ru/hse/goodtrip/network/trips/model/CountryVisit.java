package ru.hse.goodtrip.network.trips.model;

import androidx.annotation.Nullable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountryVisit {

  @Nullable
  private Integer id;

  private String country;

  private List<CityVisit> cities;
  @Nullable
  private Integer tripId;

}
