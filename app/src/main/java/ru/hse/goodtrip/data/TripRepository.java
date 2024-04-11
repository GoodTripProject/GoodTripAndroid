package ru.hse.goodtrip.data;

import android.util.Log;
import androidx.annotation.NonNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.hse.goodtrip.data.model.Result;
import ru.hse.goodtrip.data.model.ResultHolder;
import ru.hse.goodtrip.data.model.trips.Coordinates;
import ru.hse.goodtrip.data.model.trips.Country;
import ru.hse.goodtrip.network.NetworkManager;
import ru.hse.goodtrip.network.trips.TripService;
import ru.hse.goodtrip.network.trips.model.AddCountryRequest;
import ru.hse.goodtrip.network.trips.model.AddNoteRequest;
import ru.hse.goodtrip.network.trips.model.AddTripRequest;
import ru.hse.goodtrip.network.trips.model.City;
import ru.hse.goodtrip.network.trips.model.CityVisit;
import ru.hse.goodtrip.network.trips.model.CountryVisit;
import ru.hse.goodtrip.network.trips.model.Note;
import ru.hse.goodtrip.network.trips.model.Trip;

public class TripRepository {

  private static final int SRID = 4326;

  private static volatile TripRepository instance;

  private final TripService tripService;

  private TripRepository() {
    this.tripService = NetworkManager.getInstance().getInstanceOfService(TripService.class);
  }

  private String authToken;

  private List<ru.hse.goodtrip.data.model.trips.Trip> trips;

  public void setAuthToken(String authToken) {
    this.authToken = "Bearer " + authToken;
  }

  public static TripRepository getInstance() {
    if (instance == null) {
      instance = new TripRepository();
    }
    return instance;
  }

  public static ru.hse.goodtrip.data.model.trips.Trip getTripFromTripResponse(Trip tripResponse) {
    ru.hse.goodtrip.data.model.trips.Trip result = new ru.hse.goodtrip.data.model.trips.Trip(
        tripResponse.getTitle(), Collections.emptyList(),
        LocalDate.now(),
        LocalDate.now(), tripResponse.getMainPhotoUrl(), tripResponse.getMoneyInUsd(),
        new HashSet<>(), UsersRepository.getInstance()
        .getLoggedUser());
    List<ru.hse.goodtrip.data.model.trips.CountryVisit> countryVisits = getCountryVisitsFromCountryVisitResponse(
        tripResponse.getVisits());
    List<ru.hse.goodtrip.data.model.trips.Note> notes = getNotesFromNoteResponses(
        tripResponse.getNotes());
    result.setCountries(countryVisits);
    result.setNotes(notes);
    return result;
  }

  @NonNull
  public static List<ru.hse.goodtrip.data.model.trips.Note> getNotesFromNoteResponses(
      List<Note> noteResponses) {
    List<ru.hse.goodtrip.data.model.trips.Note> notes = new ArrayList<>();
    for (Note noteResponse : noteResponses) {
      notes.add(
          new ru.hse.goodtrip.data.model.trips.Note(noteResponse.getTitle(), noteResponse.getText(),
              noteResponse.getPhotoUrl(), new Country("name", new Coordinates(0, 0))));
    }
    return notes;
  }

  @NonNull
  public static List<ru.hse.goodtrip.data.model.trips.CountryVisit> getCountryVisitsFromCountryVisitResponse(
      List<CountryVisit> countryVisitResponses) {
    List<ru.hse.goodtrip.data.model.trips.CountryVisit> countryVisits = new ArrayList<>();
    for (CountryVisit visit : countryVisitResponses) {
      countryVisits.add(getCountryVisitFromCountryVisitResponse(visit));
    }
    return countryVisits;
  }

  @NonNull
  public static ru.hse.goodtrip.data.model.trips.CountryVisit getCountryVisitFromCountryVisitResponse(
      CountryVisit visit) {
    ru.hse.goodtrip.data.model.trips.CountryVisit countryVisit = new ru.hse.goodtrip.data.model.trips.CountryVisit(
        new Country(visit.getCountry(), new Coordinates(0, 0)), Collections.emptyList());
    List<ru.hse.goodtrip.data.model.trips.City> cities = new ArrayList<>();
    for (CityVisit cityVisitResponse : visit.getCities()) {
      cities.add(new ru.hse.goodtrip.data.model.trips.City(cityVisitResponse.getCity(),
          new Coordinates(0, 0),
          countryVisit.getCountry()));
    }
    countryVisit.setVisitedCities(cities);
    return countryVisit;
  }

  @NonNull
  public static AddCountryRequest getAddCountryRequestFromCountryVisit(
      ru.hse.goodtrip.data.model.trips.CountryVisit visit) {
    AddCountryRequest countryVisit = new AddCountryRequest();
    countryVisit.setCountry(visit.getCountry().getName());
    List<City> cities = new ArrayList<>();
    for (ru.hse.goodtrip.data.model.trips.City cityVisitResponse : visit.getVisitedCities()) {
      cities.add(new City(cityVisitResponse.getName(), 0.0, 0.0));
    }
    countryVisit.setCities(cities);
    return countryVisit;
  }

  public static List<ru.hse.goodtrip.data.model.trips.Trip> getTripsFromTripResponses(
      List<Trip> tripResponse) {
    List<ru.hse.goodtrip.data.model.trips.Trip> result = new ArrayList<>();
    for (Trip response : tripResponse) {
      result.add(getTripFromTripResponse(response));
    }
    return result;
  }

  private Callback<List<Trip>> getTripCallback(
      ResultHolder<List<ru.hse.goodtrip.data.model.trips.Trip>> resultHolder) {
    return new Callback<List<Trip>>() {
      /** */
      @Override
      public void onResponse(@NonNull Call<List<Trip>> call,
          @NonNull Response<List<Trip>> response) {
        Log.println(Log.DEBUG, "Response", "Trip response happened: " + response.body());
        synchronized (resultHolder) {
          List<Trip> responseBody = response.body();
          if (responseBody == null) {
            resultHolder.setResult(
                new Result.Error<>(new InterruptedException("No trips"))
            );
          } else {
            trips = getTripsFromTripResponses(responseBody);//TODO глянуть нет ли ошибки
            resultHolder.setResult(new Result.Success<>(trips));
          }
          resultHolder.notify();
        }
      }

      @Override
      public void onFailure(@NonNull Call<List<Trip>> call, @NonNull Throwable throwable) {
        Log.println(Log.DEBUG, "Response", "Trip response happened and failed: " + throwable);
        synchronized (resultHolder) {
          resultHolder.setResult(new Result.Error<>(new InterruptedException("No trips")));
          resultHolder.notify();
        }
      }
    };
  }

  private <T> Callback<T> getCallback(ResultHolder<T> resultHolder, String errorMessage) {
    return new Callback<T>() {
      @Override
      public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
        synchronized (resultHolder) {
          T responseBody = response.body();
          if (responseBody == null) {
            resultHolder.setResult(
                new Result.Error<>(new InterruptedException(errorMessage))
            );
          } else {
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

  public ResultHolder<List<ru.hse.goodtrip.data.model.trips.Trip>> getUserTrips(Integer userId,
      String token) {
    Log.println(Log.WARN, "hey", "trips");
    ResultHolder<List<ru.hse.goodtrip.data.model.trips.Trip>> resultHolder = new ResultHolder<>();
    Call<List<Trip>> getTripsCall = tripService.getUserTrips(userId, getWrappedToken(token));
    getTripsCall.enqueue(getTripCallback(resultHolder));
    return resultHolder;
  }

  private String getWrappedToken(String token) {
    return "Bearer " + token;
  }


  public ResultHolder<Object> getTripById(Integer tripId) {//TODO возвращаемый тип
    ResultHolder<Object> resultHolder = new ResultHolder<>();
    Call<Object> getTripCall = tripService.getTripById(tripId, authToken);
    getTripCall.enqueue(getCallback(resultHolder, "Trip with this id not exists"));
    return resultHolder;
  }

  public ResultHolder<String> addTrip(Integer userId, String token,
      AddTripRequest addTripRequest) {
    ResultHolder<String> resultHolder = new ResultHolder<>();
    Call<String> addTripCall = tripService.addTrip(userId, addTripRequest,
        getWrappedToken(token));
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
