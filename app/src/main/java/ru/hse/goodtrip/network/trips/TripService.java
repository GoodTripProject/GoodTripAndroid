package ru.hse.goodtrip.network.trips;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import ru.hse.goodtrip.data.model.tripRequests.model.AddCountryRequest;
import ru.hse.goodtrip.data.model.tripRequests.model.AddNoteRequest;
import ru.hse.goodtrip.data.model.tripRequests.model.AddTripRequest;
import ru.hse.goodtrip.data.model.trips.Trip;

public interface TripService {
    @GET("/trip/all/{userId}")
    Call<List<Trip>> getUserTrips(@Path("userId") Integer userId);

    @GET("/trip/{tripId}")
    Call<Object> getTripById(@Path("tripId") Integer tripId);

    @POST("/trip/{userId}")
    Call<String> addTrip(@Path("userId") Integer userId, @Body AddTripRequest addTripRequest);

    @DELETE("/trip/{tripId}")
    Call<String> deleteTripById(@Path("tripId") Integer tripId);

    @GET("/trip/note/{noteId}")
    Call<Object> getNoteById(@Path("noteId") Integer noteId);

    @DELETE("/trip/note/{noteId}")
    Call<String> deleteNoteById(@Path("noteId") Integer noteId);

    @POST("/trip/note/{userId}")
    Call<String> addNote(@Path("userId") Integer userId, @Body AddNoteRequest addNoteRequest);

    @POST("/trip/country/{tripId}")
    Call<String> addCountryVisit(@Path("tripId") Integer tripId, @Body AddCountryRequest addCountryRequest);

    @DELETE("/trip/country/{countryVisitId}")
    Call<String> deleteCountryVisit(@Path("countryVisitId") Integer countryVisitId);

}
