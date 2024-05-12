package ru.hse.goodtrip.network.places;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import ru.hse.goodtrip.network.places.model.PlaceRequest;

public interface PlacesService {

  @POST("/places")
  Call<Object> getNearPlaces(@Body PlaceRequest placeRequest,@Header("Authorization") String authorization);
}
