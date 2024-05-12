package ru.hse.goodtrip.network.places.model;

import androidx.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaceRequest {
  private String location;
  private int radius;
  @Nullable
  private String rankBy;
  @Nullable
  private PlacesTypes type;
}
enum PlacesTypes {
  amusement_park,
  aquarium,
  art_gallery,
  cafe,
  casino,
  church,
  city_hall,
  hindu_temple,
  library,
  local_government_office,
  mosque,
  movie_theater,
  museum,
  night_club,
  park,
  restaurant,
  stadium,
  subway_station,
  synagogue,
  tourist_attraction,
  zoo
}
