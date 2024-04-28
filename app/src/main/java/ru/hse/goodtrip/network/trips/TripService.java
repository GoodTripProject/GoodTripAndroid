package ru.hse.goodtrip.network.trips;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import ru.hse.goodtrip.network.trips.model.AddCountryRequest;
import ru.hse.goodtrip.network.trips.model.AddNoteRequest;
import ru.hse.goodtrip.network.trips.model.AddTripRequest;
import ru.hse.goodtrip.network.trips.model.Trip;
import ru.hse.goodtrip.network.trips.model.TripView;

public interface TripService {

  @GET("/trip/all/{userId}")
  Call<List<Trip>> getUserTrips(@Path("userId") Integer userId,
      @Header("Authorization") String authorization);

  @GET("/trip/{tripId}")
  Call<Object> getTripById(@Path("tripId") Integer tripId,
      @Header("Authorization") String authorization);

  @POST("/trip/{userId}")
  Call<String> addTrip(@Path("userId") Integer userId, @Body AddTripRequest addTripRequest,
      @Header("Authorization") String authorization);

  @DELETE("/trip/{tripId}")
  Call<String> deleteTripById(@Path("tripId") Integer tripId,
      @Header("Authorization") String authorization);

  @GET("/trip/note/{noteId}")
  Call<Object> getNoteById(@Path("noteId") Integer noteId,
      @Header("Authorization") String authorization);

  @DELETE("/trip/note/{noteId}")
  Call<String> deleteNoteById(@Path("noteId") Integer noteId,
      @Header("Authorization") String authorization);

  @POST("/trip/note/{userId}")
  Call<String> addNote(@Path("userId") Integer userId, @Body AddNoteRequest addNoteRequest,
      @Header("Authorization") String authorization);

  @POST("/trip/country/{tripId}")
  Call<String> addCountryVisit(@Path("tripId") Integer tripId,
      @Body AddCountryRequest addCountryRequest, @Header("Authorization") String authorization);

  @DELETE("/trip/country/{countryVisitId}")
  Call<String> deleteCountryVisit(@Path("countryVisitId") Integer countryVisitId,
      @Header("Authorization") String authorization);

  @PUT("/update_trip")
  Call<String> updateTrip(@Body Trip trip);

  @GET("get_authors_trips/?userId={userId}&start={start}")
  Call<List<TripView>> getAuthorsTrips(@Path("userId") Integer userId,
      @Path("start") Integer start);

}
