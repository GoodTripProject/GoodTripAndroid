package ru.hse.goodtrip.data;

import android.util.Log;
import androidx.annotation.NonNull;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import lombok.Getter;
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


  @Getter
  private List<ru.hse.goodtrip.data.model.trips.Trip> trips = new ArrayList<>();

  public static TripRepository getInstance() {
    if (instance == null) {
      instance = new TripRepository();
    }
    return instance;
  }

  /**
   * Convert trip from network to trip.
   *
   * @param tripResponse Trip response.
   * @return Trip.
   */
  public static ru.hse.goodtrip.data.model.trips.Trip getTripFromTripResponse(Trip tripResponse) {

    ru.hse.goodtrip.data.model.trips.Trip result = new ru.hse.goodtrip.data.model.trips.Trip(
        tripResponse.getTitle(), Collections.emptyList(),
        tripResponse.getDepartureDate().toInstant()
            .atZone(ZoneId.systemDefault()).toLocalDate(),
        tripResponse.getArrivalDate().toInstant()
            .atZone(ZoneId.systemDefault()).toLocalDate(),
        tripResponse.getPublicationTimestamp().toInstant()
            .atZone(ZoneId.systemDefault()).toLocalDate(),
        tripResponse.getMainPhotoUrl(), tripResponse.getMoneyInUsd(),
        new HashSet<>(), UsersRepository.getInstance()
        .getLoggedUser(), tripResponse.getId());
    List<ru.hse.goodtrip.data.model.trips.CountryVisit>
        countryVisits = getCountryVisitsFromCountryVisitResponse(
        tripResponse.getVisits());
    List<ru.hse.goodtrip.data.model.trips.Note>
        notes = getNotesFromNoteResponses(
        tripResponse.getNotes());
    result.setCountries(countryVisits);
    result.setNotes(notes);
    return result;
  }

  /**
   * Converts noteResponses to notes.
   *
   * @param noteResponses Notes from network.
   * @return List of notes.
   */
  @NonNull
  public static List<ru.hse.goodtrip.data.model.trips.Note> getNotesFromNoteResponses(
      List<Note> noteResponses) {
    List<ru.hse.goodtrip.data.model.trips.Note> notes = new ArrayList<>();
    for (Note noteResponse : noteResponses) {
      notes.add(
          new ru.hse.goodtrip.data.model.trips.Note(noteResponse.getTitle(), noteResponse.getText(),
              noteResponse.getPhotoUrl(),
              new Country(noteResponse.getGooglePlaceId(),
                  new Coordinates(0, 0))));
    }
    return notes;
  }

  /**
   * Converts country visit response to country visit.
   *
   * @param countryVisitResponses Country visit responses.
   * @return List of country visit.
   */
  @NonNull
  public static List<ru.hse.goodtrip.data.model.trips.CountryVisit> getCountryVisitsFromCountryVisitResponse(
      List<CountryVisit> countryVisitResponses) {
    List<ru.hse.goodtrip.data.model.trips.CountryVisit> countryVisits = new ArrayList<>();
    for (CountryVisit visit : countryVisitResponses) {
      countryVisits.add(
          getCountryVisitFromCountryVisitResponse(visit));
    }
    return countryVisits;
  }

  /**
   * Converts CountryVisit response to CountryVisit.
   *
   * @param visit CountryVisit response.
   * @return CountryVisit.
   */
  @NonNull
  public static ru.hse.goodtrip.data.model.trips.CountryVisit getCountryVisitFromCountryVisitResponse(
      CountryVisit visit) {
    ru.hse.goodtrip.data.model.trips.CountryVisit countryVisit
        = new ru.hse.goodtrip.data.model.trips.CountryVisit(
        new Country(visit.getCountry(),
            new Coordinates(0, 0)),
        Collections.emptyList());
    List<ru.hse.goodtrip.data.model.trips.City> cities = new ArrayList<>();
    for (CityVisit cityVisitResponse : visit.getCities()) {
      cities.add(new
          ru.hse.goodtrip.data.model.trips.City(
          cityVisitResponse.getCity(),
          new Coordinates(0, 0),
          countryVisit.getCountry()));
    }
    countryVisit.setVisitedCities(cities);
    return countryVisit;
  }

  /**
   * Converts CountryVisit to AddCountryRequest.
   *
   * @param visit CountryVisit.
   * @return Add Country Request.
   */
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

  /**
   * Converts TripResponses to Trips.
   *
   * @param tripResponse trips from network.
   * @return Returns list of trips.
   */
  public static List<ru.hse.goodtrip.data.model.trips.Trip> getTripsFromTripResponses(
      List<Trip> tripResponse) {
    List<ru.hse.goodtrip.data.model.trips.Trip> result = new ArrayList<>();
    for (Trip response : tripResponse) {
      result.add(getTripFromTripResponse(response));
    }
    return result;
  }

  /**
   * Make a trip callback.
   *
   * @param resultHolder result Holder.
   * @return callback.
   */
  private Callback<List<Trip>> getTripCallback(
      ResultHolder<List<ru.hse.goodtrip.data.model.trips.Trip>> resultHolder) {
    return new Callback<List<Trip>>() {
      /** */
      @Override
      public void onResponse(@NonNull Call<List<Trip>> call,
          @NonNull Response<List<Trip>> response) {
        Log.println(Log.WARN, "Response", "Trip response happened: " + response.body());
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
        Log.println(Log.WARN, "Response", "Trip response happened and failed: " + throwable);
        synchronized (resultHolder) {
          resultHolder.setResult(new Result.Error<>(new InterruptedException("No trips")));
          resultHolder.notify();
        }
      }
    };
  }

  /**
   * Make a callback.
   *
   * @param resultHolder result Holder.
   * @return callback.
   */
  private <T> Callback<T> getCallback(ResultHolder<T> resultHolder, String errorMessage) {
    return new Callback<T>() {
      @Override
      public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
        Log.println(Log.DEBUG, "Response", "Response " + response.body());
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
        Log.println(Log.DEBUG, "Response", "Response failed" + throwable);
        synchronized (resultHolder) {
          resultHolder.setResult(new Result.Error<>(new InterruptedException(errorMessage)));
          resultHolder.notify();
        }
      }
    };
  }

  /**
   * Make request to the server to get trips.
   *
   * @param userId User id.
   * @param token  Jwt token.
   * @return Result Holder of trips.
   */
  public ResultHolder<List<ru.hse.goodtrip.data.model.trips.Trip>> getUserTrips(Integer userId,
      String token) {
    Log.println(Log.WARN, "hey", "trips");
    ResultHolder<List<ru.hse.goodtrip.data.model.trips.Trip>> resultHolder = new ResultHolder<>();
    Call<List<Trip>> getTripsCall = tripService.getUserTrips(userId, getWrappedToken(token));
    getTripsCall.enqueue(getTripCallback(resultHolder));
    return resultHolder;
  }

  /**
   * Wraps token.
   *
   * @param token Bare jwt token.
   * @return Wrapped token.
   */
  private String getWrappedToken(String token) {
    return "Bearer " + token;
  }


  /**
   * Make request to the server to get trip.
   *
   * @param tripId Id of trip.
   * @param token  Jwt token.
   * @return ResultHolder of trip.
   */
  public ResultHolder<Object> getTripById(Integer tripId, String token) {//TODO возвращаемый тип
    ResultHolder<Object> resultHolder = new ResultHolder<>();
    Call<Object> getTripCall = tripService.getTripById(tripId, getWrappedToken(token));
    getTripCall.enqueue(getCallback(resultHolder, "Trip with this id not exists"));
    return resultHolder;
  }

  /**
   * Make request to the server to add trip.
   *
   * @param userId         Id of user.
   * @param token          Jwt token.
   * @param addTripRequest Request.
   * @return ResultHolder of String which holds result of request.
   */
  public ResultHolder<String> addTrip(Integer userId, String token,
      AddTripRequest addTripRequest) {
    ResultHolder<String> resultHolder = new ResultHolder<>();
    Call<String> addTripCall = tripService.addTrip(userId, addTripRequest,
        getWrappedToken(token));
    addTripCall.enqueue(getCallback(resultHolder, "User with this id not exists"));
    return resultHolder;
  }

  /**
   * Make request to the server to delete trip.
   *
   * @param tripId Id of trip.
   * @param token  Jwt token.
   * @return ResultHolder of String which holds result of request.
   */
  public ResultHolder<String> deleteTrip(Integer tripId, String token) {
    ResultHolder<String> resultHolder = new ResultHolder<>();
    Call<String> deleteTripCall = tripService.deleteTripById(tripId, getWrappedToken(token));
    deleteTripCall.enqueue(getCallback(resultHolder, "Trip with this id not exists"));
    return resultHolder;
  }

  /**
   * Make request to the server to get note by id.
   *
   * @param noteId Id of note.
   * @param token  Jwt token.
   * @return ResultHolder.
   */
  public ResultHolder<Object> getNoteById(Integer noteId, String token) {
    ResultHolder<Object> resultHolder = new ResultHolder<>();
    Call<Object> getTripCall = tripService.getNoteById(noteId, getWrappedToken(token));
    getTripCall.enqueue(getCallback(resultHolder, "Note with this id not exists"));
    return resultHolder;
  }

  /**
   * Make request to the server to delete note by id.
   *
   * @param noteId Id of note.
   * @param token  Jwt token.
   * @return ResultHolder.
   */
  public ResultHolder<String> deleteNoteById(Integer noteId, String token) {
    ResultHolder<String> resultHolder = new ResultHolder<>();
    Call<String> deleteNoteCall = tripService.deleteNoteById(noteId, getWrappedToken(token));
    deleteNoteCall.enqueue(getCallback(resultHolder, "Note with this id not exists"));
    return resultHolder;
  }

  /**
   * Make request to the server to add note.
   *
   * @param userId         Id of user.
   * @param token          Jwt token.
   * @param addNoteRequest AddNoteRequest.
   * @return ResultHolder of String which holds result of request.
   */
  public ResultHolder<String> addNote(Integer userId, String token, AddNoteRequest addNoteRequest) {
    ResultHolder<String> resultHolder = new ResultHolder<>();
    Call<String> addNoteCall = tripService.addNote(userId, addNoteRequest, getWrappedToken(token));
    addNoteCall.enqueue(getCallback(resultHolder, "User with this id not exist"));
    return resultHolder;
  }

  /**
   * Make request to the server to add country visit.
   *
   * @param tripId            Id of trip.
   * @param token             Jwt token.
   * @param addCountryRequest AddCountryRequest.
   * @return ResultHolder of String which holds result of request.
   */
  public ResultHolder<String> addCountryVisit(Integer tripId, String token,
      AddCountryRequest addCountryRequest) {
    ResultHolder<String> resultHolder = new ResultHolder<>();
    Call<String> addCountryCall = tripService.addCountryVisit(tripId, addCountryRequest,
        getWrappedToken(token));
    addCountryCall.enqueue(getCallback(resultHolder, "Trip with this id not exist"));
    return resultHolder;
  }

  /**
   * Make request to the server to delete country visit.
   *
   * @param countryVisitId Id of trip.
   * @param token          Jwt token.
   * @return ResultHolder of String which holds result of request.
   */
  public ResultHolder<String> deleteCountryVisit(Integer countryVisitId, String token) {
    ResultHolder<String> resultHolder = new ResultHolder<>();
    Call<String> deleteCountryCall = tripService.deleteCountryVisit(countryVisitId,
        getWrappedToken(token));
    deleteCountryCall.enqueue(getCallback(resultHolder, "Country with this id not exist"));
    return resultHolder;
  }

}