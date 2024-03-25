package ru.hse.goodtrip.data;

import androidx.annotation.NonNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.hse.goodtrip.data.model.Result;
import ru.hse.goodtrip.data.model.ResultHolder;
import ru.hse.goodtrip.data.model.tripRequests.model.AddCountryRequest;
import ru.hse.goodtrip.data.model.tripRequests.model.AddNoteRequest;
import ru.hse.goodtrip.data.model.tripRequests.model.AddTripRequest;
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


    public ResultHolder<Object> getTripById(Integer tripId) {//TODO возвращаемый тип
        ResultHolder<Object> resultHolder = new ResultHolder<>();
        Call<Object> getTripCall = tripService.getTripById(tripId, authToken);
        getTripCall.enqueue(getCallback(resultHolder, "Trip with this id not exists"));
        return resultHolder;
    }

    public ResultHolder<String> addTrip(Integer userId, AddTripRequest addTripRequest) {
        ResultHolder<String> resultHolder = new ResultHolder<>();
        Call<String> addTripCall = tripService.addTrip(userId, addTripRequest, authToken);
        addTripCall.enqueue(getCallback(resultHolder, "User with this id not exists"));
        return resultHolder;
    }

    public ResultHolder<String> deleteTrip(Integer tripId) {
        ResultHolder<String> resultHolder = new ResultHolder<>();
        Call<String> deleteTripCall = tripService.deleteTripById(tripId, authToken);
        deleteTripCall.enqueue(getCallback(resultHolder, "Trip with this id not exists"));
        return resultHolder;
    }

    public ResultHolder<Object> getNoteById(Integer noteId) {
        ResultHolder<Object> resultHolder = new ResultHolder<>();
        Call<Object> getTripCall = tripService.getNoteById(noteId, authToken);
        getTripCall.enqueue(getCallback(resultHolder, "Note with this id not exists"));
        return resultHolder;
    }

    public ResultHolder<String> deleteNoteById(Integer noteId) {
        ResultHolder<String> resultHolder = new ResultHolder<>();
        Call<String> deleteNoteCall = tripService.deleteNoteById(noteId, authToken);
        deleteNoteCall.enqueue(getCallback(resultHolder, "Note with this id not exists"));
        return resultHolder;
    }

    public ResultHolder<String> addNote(Integer userId, AddNoteRequest addNoteRequest) {
        ResultHolder<String> resultHolder = new ResultHolder<>();
        Call<String> addNoteCall = tripService.addNote(userId, addNoteRequest, authToken);
        addNoteCall.enqueue(getCallback(resultHolder, "User with this id not exist"));
        return resultHolder;
    }

    public ResultHolder<String> addCountryVisit(Integer tripId, AddCountryRequest addCountryRequest) {
        ResultHolder<String> resultHolder = new ResultHolder<>();
        Call<String> addCountryCall = tripService.addCountryVisit(tripId, addCountryRequest, authToken);
        addCountryCall.enqueue(getCallback(resultHolder, "Trip with this id not exist"));
        return resultHolder;
    }

    public ResultHolder<String> deleteCountryVisit(Integer countryVisitId) {
        ResultHolder<String> resultHolder = new ResultHolder<>();
        Call<String> deleteCountryCall = tripService.deleteCountryVisit(countryVisitId, authToken);
        deleteCountryCall.enqueue(getCallback(resultHolder, "Country with this id not exist"));
        return resultHolder;
    }

}
