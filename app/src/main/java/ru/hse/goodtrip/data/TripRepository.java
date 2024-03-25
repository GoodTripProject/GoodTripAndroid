package ru.hse.goodtrip.data;

import java.io.IOException;
import java.util.List;

import lombok.Setter;
import retrofit2.Call;
import retrofit2.Response;
import ru.hse.goodtrip.data.model.tripRequests.model.AddCountryRequest;
import ru.hse.goodtrip.data.model.tripRequests.model.AddNoteRequest;
import ru.hse.goodtrip.data.model.tripRequests.model.AddTripRequest;
import ru.hse.goodtrip.data.model.trips.Note;
import ru.hse.goodtrip.data.model.trips.Trip;
import ru.hse.goodtrip.network.NetworkManager;
import ru.hse.goodtrip.network.trips.TripService;

public class TripRepository {
    private static volatile TripRepository instance;

    private final TripService tripService;

    private TripRepository() {
        this.tripService = NetworkManager.getInstance().getInstanceOfService(TripService.class);
    }

    @Setter
    private String userToken;

    public static TripRepository getInstance() {
        if (instance == null) {
            instance = new TripRepository();
        }
        return instance;
    }

    private <T> T sendRequest(Call<T> call) {
        try {
            call.wait();
            Response<T> response = call.execute();
            if (!response.isSuccessful()) {
                return null; //TODO заменить на Result
            }
            return response.body();
        } catch (InterruptedException | IOException e) {
            return null;
        }
    }

    public List<Trip> getUserTrips(Integer userId) {
        Call<List<Trip>> getTripsCall = tripService.getUserTrips(userId);
        return sendRequest(getTripsCall);
    }

    public Trip getTripById(Integer tripId) {
        Call<Object> getTripCall = tripService.getTripById(tripId);
        return (Trip) sendRequest(getTripCall);
    }

    public String addTrip(Integer userId, AddTripRequest addTripRequest) {
        Call<String> addTripCall = tripService.addTrip(userId, addTripRequest);
        return sendRequest(addTripCall);
    }

    public String deleteTrip(Integer tripId) {
        Call<String> deleteTripCall = tripService.deleteTripById(tripId);
        return sendRequest(deleteTripCall);
    }

    public Note getNoteById(Integer noteId) {
        Call<Object> getTripCall = tripService.getNoteById(noteId);
        return (Note) sendRequest(getTripCall);
    }

    public String deleteNoteById(Integer noteId) {
        Call<String> deleteNoteCall = tripService.deleteNoteById(noteId);
        return sendRequest(deleteNoteCall);
    }

    public String addNote(Integer userId, AddNoteRequest addNoteRequest) {
        Call<String> addNoteCall = tripService.addNote(userId, addNoteRequest);
        return sendRequest(addNoteCall);
    }

    public String addCountryVisit(Integer tripId, AddCountryRequest addCountryRequest) {
        Call<String> addCountryCall = tripService.addCountryVisit(tripId, addCountryRequest);
        return sendRequest(addCountryCall);
    }

    public String deleteCountryVisit(Integer countryVisitId) {
        Call<String> deleteCountryCall = tripService.deleteCountryVisit(countryVisitId);
        return sendRequest(deleteCountryCall);
    }

}
