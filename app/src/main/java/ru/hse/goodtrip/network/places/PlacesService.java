package ru.hse.goodtrip.network.places;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
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
   * @return Returns String when error happens, otherwise returns List of PlacesResponse.
   */
  @POST("/places")
  Call<Object> getNearPlaces(@Body PlaceRequest placeRequest,
      @Header("Authorization") String authorization);

  @GET("/coordinates")
  Call<Object> getCoordinates(@Query("city") String city,
      @Header("Authorization") String authorization);
}
