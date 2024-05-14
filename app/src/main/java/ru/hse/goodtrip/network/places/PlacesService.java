package ru.hse.goodtrip.network.places;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import ru.hse.goodtrip.network.places.model.PlaceRequest;

/**
 * Service to work with places API.
 */
public interface PlacesService {

  /**
   * Get near places.
   *
   * @param placeRequest  PlaceRequest.
   * @param authorization Jwt token.
   * @return Returns String when error happens, otherwise returns List<PlacesResponse>.
   */
  @POST("/places")
  Call<Object> getNearPlaces(@Body PlaceRequest placeRequest,
      @Header("Authorization") String authorization);
}
