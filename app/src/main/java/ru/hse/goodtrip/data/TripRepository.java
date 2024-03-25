package ru.hse.goodtrip.data;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.hse.goodtrip.data.model.Result;
import ru.hse.goodtrip.data.model.ResultHolder;
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

    private String authToken;

    private List<Trip> trips;

    public void setAuthToken(String authToken) {
        this.authToken = "Bearer " + authToken;
    }

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

    private <T> Callback<T> getCallback(ResultHolder<T> resultHolder, String errorMessage) {
        return new Callback<T>() {
            /** @noinspection unchecked*/
            @Override
            public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
                synchronized (resultHolder) {
                    T responseBody = response.body();
                    if (responseBody == null) {
                        resultHolder.setResult(
                                new Result.Error<>(new InterruptedException(errorMessage))
                        );
                    } else {
                        if (responseBody instanceof List) {
                            trips = (List<Trip>) responseBody;//TODO глянуть нет ли ошибки
                        }
                        resultHolder.setResult(new Result.Success<>(responseBody));
                    }
                    resultHolder.notify();
                }
            }

            @Override
            public void onFailure(@NonNull Call<T> call, @NonNull Throwable throwable) {
                synchronized (resultHolder) {
                    resultHolder.setResult(new Result.Error<>(new InterruptedException(errorMessage)));
                    resultHolder.notify();
                }
            }
        };
    }

    public ResultHolder<List<Trip>> getUserTrips(Integer userId) {
        ResultHolder<List<Trip>> resultHolder = new ResultHolder<>();
        Call<List<Trip>> getTripsCall = tripService.getUserTrips(userId, authToken);
        getTripsCall.enqueue(getCallback(resultHolder, "No trips"));
        return resultHolder;
    }


    public Trip getTripById(Integer tripId) {
        Call<Object> getTripCall = tripService.getTripById(tripId, authToken);
        return (Trip) sendRequest(getTripCall);
    }

    public String addTrip(Integer userId, AddTripRequest addTripRequest) {
        Call<String> addTripCall = tripService.addTrip(userId, addTripRequest, authToken);
        return sendRequest(addTripCall);
    }

    public String deleteTrip(Integer tripId) {
        Call<String> deleteTripCall = tripService.deleteTripById(tripId, authToken);
        return sendRequest(deleteTripCall);
    }

    public Note getNoteById(Integer noteId) {
        Call<Object> getTripCall = tripService.getNoteById(noteId, authToken);
        return (Note) sendRequest(getTripCall);
    }

    public String deleteNoteById(Integer noteId) {
        Call<String> deleteNoteCall = tripService.deleteNoteById(noteId, authToken);
        return sendRequest(deleteNoteCall);
    }

    public String addNote(Integer userId, AddNoteRequest addNoteRequest) {
        Call<String> addNoteCall = tripService.addNote(userId, addNoteRequest, authToken);
        return sendRequest(addNoteCall);
    }

    public String addCountryVisit(Integer tripId, AddCountryRequest addCountryRequest) {
        Call<String> addCountryCall = tripService.addCountryVisit(tripId, addCountryRequest, authToken);
        return sendRequest(addCountryCall);
    }

    public String deleteCountryVisit(Integer countryVisitId) {
        Call<String> deleteCountryCall = tripService.deleteCountryVisit(countryVisitId, authToken);
        return sendRequest(deleteCountryCall);
    }

}
