package ru.hse.goodtrip.data;

import androidx.annotation.NonNull;
import java.sql.Date;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.Getter;
import org.locationtech.jts.geom.Point;
import retrofit2.Call;
import ru.hse.goodtrip.data.model.Result;
import ru.hse.goodtrip.data.model.Result.Success;
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
import ru.hse.goodtrip.network.trips.model.TripView;

public class TripRepository extends AbstractRepository {


  private static volatile TripRepository instance;

  private final TripService tripService;
  @Getter
  private List<ru.hse.goodtrip.data.model.trips.Trip> userTrips = new ArrayList<>();
  @Getter
  private List<TripView> authorTrips = new ArrayList<>();

  private TripRepository() {
    super();
    this.tripService = NetworkManager.getInstance().getInstanceOfService(TripService.class);
  }

  public static TripRepository getInstance() {
    if (instance == null) {
      instance = new TripRepository();
    }
    return instance;
  }

  private static CompletableFuture<Point> getCoordinates(String name) {
    return PlacesRepository.getInstance().getPlaceCoordinate(name,
            UsersRepository.getInstance().getLoggedUser().getToken())
        .thenApply(pointResult -> {
          if (pointResult instanceof Result.Success) {
            return ((Success<Point>) pointResult).getData();
          }
          return createNewPoint(0, 0);
        });
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
        .getLoggedUser(), tripResponse.getId(), tripResponse.getState());
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

  private static Note getNetworkNoteFromNote(int tripId,
      ru.hse.goodtrip.data.model.trips.Note noteModel) {
    return new Note(null, noteModel.getHeadline(), noteModel.getPhotoUrl(),
        noteModel.getPlace().getName(),
        noteModel.getNote(), tripId);
  }

  private static CountryVisit getNetworkCountryVisitFromCountryVisit(int tripId,
      ru.hse.goodtrip.data.model.trips.CountryVisit countryVisit) {
    return new CountryVisit(null,
        countryVisit.getCountry().getName(),
        countryVisit.getVisitedCities().stream().map(
            TripRepository::getNetworkCityVisitFromCityVisit).collect(Collectors.toList()), tripId);
  }

  private static CityVisit getNetworkCityVisitFromCityVisit(
      ru.hse.goodtrip.data.model.trips.City city) {
    return new CityVisit(null, city.getName(),
        createNewPoint(city.getCoordinates().getLatitude(), city.getCoordinates().getLongitude())
        , null);
  }

  /**
   * Convert trip from network to trip.
   *
   * @param tripModel Trip.
   * @return Trip network.
   */
  public static Trip getNetworkTripFromTrip(int userId,
      ru.hse.goodtrip.data.model.trips.Trip tripModel) {
    return new Trip(tripModel.getTripId(), userId,
        tripModel.getTitle(), tripModel.getMoneyInUsd(), tripModel.getMainPhotoUrl(),
        Date.valueOf(tripModel.getStartTripDate().toString()),
        Date.valueOf(tripModel.getEndTripDate().toString()), null,
        tripModel.getTripState(),
        tripModel.getNotes().stream()
            .map(note -> getNetworkNoteFromNote(tripModel.getTripId(), note)).collect(
                Collectors.toList()),
        tripModel.getCountries().stream().map(
            countryVisit -> getNetworkCountryVisitFromCountryVisit(tripModel.getTripId(),
                countryVisit)).collect(Collectors.toList()));
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
      getCoordinates(noteResponse.getGooglePlaceId()).thenAcceptAsync(point -> {
        synchronized (notes) {
          notes.add(
              new ru.hse.goodtrip.data.model.trips.Note(noteResponse.getTitle(),
                  noteResponse.getText(),
                  noteResponse.getPhotoUrl(),
                  new Country(noteResponse.getGooglePlaceId(),
                      new Coordinates(point.getX(), point.getY())
                  )
              )
          );
        }
      });
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
          new Coordinates(cityVisitResponse.getPoint().getX(), cityVisitResponse.getPoint().getY()),
          countryVisit.getCountry()));
    }
    if (!cities.isEmpty()) {
      countryVisit.setCountry(new Country(countryVisit.getCountry().getName(),
          cities.get(0).getCoordinates()));
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
    List<CompletableFuture<Void>> futures = new ArrayList<>();
    for (ru.hse.goodtrip.data.model.trips.City cityVisitResponse : visit.getVisitedCities()) {
      futures.add(getCoordinates(countryVisit.getCountry() + " " + cityVisitResponse.getName())
          .thenAcceptAsync(point -> {
            synchronized (cities) {
              cities.add(new City(cityVisitResponse.getName(), point.getX(), point.getY()));
            }
          }));
    }
    for (CompletableFuture<Void> future : futures) {
      future.join();
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

  public void resetAuthorTrips() {
    authorTrips = new ArrayList<>();
  }

  /**
   * Make request to the server to get trips.
   *
   * @param userId User id.
   * @param token  Jwt token.
   * @return CompletableFuture of Result of trips.
   */
  public CompletableFuture<Result<List<Trip>>> getUserTrips(
      Integer userId,
      String token) {
    ResultHolder<List<Trip>> resultHolder = new ResultHolder<>();
    Call<List<Trip>> getTripsCall = tripService.getUserTrips(userId, getWrappedToken(token));
    getTripsCall.enqueue(
        getCallback(resultHolder, "", (result) -> userTrips = getTripsFromTripResponses(result)));
    return getCompletableFuture(resultHolder);
  }

  /**
   * Make request to the server to get trips of authors.
   *
   * @param userId User id.
   * @param token  Jwt token.
   * @return CompletableFuture of Result of trips.
   */
  public CompletableFuture<Result<List<TripView>>> getAuthorsTrips(
      Integer userId,
      String token) {
    ResultHolder<List<TripView>> resultHolder = new ResultHolder<>();
    Call<List<TripView>> getTripsCall = tripService.getAuthorsTrips(userId, authorTrips.size(),
        getWrappedToken(token));
    getTripsCall.enqueue(getCallback(resultHolder, "", (result) -> authorTrips.addAll(result)));
    return getCompletableFuture(resultHolder);
  }


  /**
   * Make request to the server to get trip.
   *
   * @param tripId Id of trip.
   * @param token  Jwt token.
   * @return CompletableFuture of Result of trip.
   */
  public CompletableFuture<Result<Object>> getTripById(Integer tripId,
      String token) {
    ResultHolder<Object> resultHolder = new ResultHolder<>();
    Call<Object> getTripCall = tripService.getTripById(tripId, getWrappedToken(token));
    getTripCall.enqueue(getCallback(resultHolder, "Trip with this id not exists", (result) -> {
    }));
    return getCompletableFuture(resultHolder);
  }

  /**
   * Make request to the server to add trip.
   *
   * @param userId         Id of user.
   * @param token          Jwt token.
   * @param addTripRequest Request.
   * @return CompletableFuture of Result of String which holds result of request.
   */
  public CompletableFuture<Result<String>> addTrip(Integer userId, String token,
      AddTripRequest addTripRequest) {
    ResultHolder<String> resultHolder = new ResultHolder<>();
    Call<String> addTripCall = tripService.addTrip(userId, addTripRequest,
        getWrappedToken(token));
    addTripCall.enqueue(getCallback(resultHolder, "User with this id not exists", (result) -> {
    }));
    return getCompletableFuture(resultHolder);
  }

  /**
   * Make request to the server to delete trip.
   *
   * @param tripId Id of trip.
   * @param token  Jwt token.
   * @return CompletableFuture of Result of String which holds result of request.
   */
  public CompletableFuture<Result<String>> deleteTrip(Integer tripId, String token) {
    ResultHolder<String> resultHolder = new ResultHolder<>();
    Call<String> deleteTripCall = tripService.deleteTripById(tripId, getWrappedToken(token));
    deleteTripCall.enqueue(getCallback(resultHolder, "Trip with this id not exists", (result) -> {
    }));
    return getCompletableFuture(resultHolder);
  }

  /**
   * Make request to the server to get note by id.
   *
   * @param noteId Id of note.
   * @param token  Jwt token.
   * @return CompletableFuture of Result.
   */
  public CompletableFuture<Result<Object>> getNoteById(Integer noteId, String token) {
    ResultHolder<Object> resultHolder = new ResultHolder<>();
    Call<Object> getTripCall = tripService.getNoteById(noteId, getWrappedToken(token));
    getTripCall.enqueue(getCallback(resultHolder, "Note with this id not exists", (result) -> {
    }));
    return getCompletableFuture(resultHolder);
  }

  /**
   * Make request to the server to delete note by id.
   *
   * @param noteId Id of note.
   * @param token  Jwt token.
   * @return CompletableFuture of Result.
   */
  public CompletableFuture<Result<String>> deleteNoteById(Integer noteId, String token) {
    ResultHolder<String> resultHolder = new ResultHolder<>();
    Call<String> deleteNoteCall = tripService.deleteNoteById(noteId, getWrappedToken(token));
    deleteNoteCall.enqueue(getCallback(resultHolder, "Note with this id not exists", (result) -> {
    }));
    return getCompletableFuture(resultHolder);
  }

  /**
   * Make request to the server to add note.
   *
   * @param userId         Id of user.
   * @param token          Jwt token.
   * @param addNoteRequest AddNoteRequest.
   * @return CompletableFuture of Result of String which holds result of request.
   */
  public CompletableFuture<Result<String>> addNote(Integer userId, String token,
      AddNoteRequest addNoteRequest) {
    ResultHolder<String> resultHolder = new ResultHolder<>();
    Call<String> addNoteCall = tripService.addNote(userId, addNoteRequest, getWrappedToken(token));
    addNoteCall.enqueue(getCallback(resultHolder, "User with this id not exist", (result) -> {
    }));
    return getCompletableFuture(resultHolder);
  }

  /**
   * Make request to update trip.
   *
   * @param trip  New version of trip.
   * @param token Jwt token.
   * @return CompletableFuture of Result of String which holds result of request.
   */
  public CompletableFuture<Result<String>> updateTrip(Trip trip, String token) {
    ResultHolder<String> resultHolder = new ResultHolder<>();
    Call<String> updateTripCall = tripService.updateTrip(trip.getUserId(), trip,
        getWrappedToken(token));
    updateTripCall.enqueue(
        getCallback(resultHolder, "User or trip with this id not exist", (result) -> {
        }));
    return getCompletableFuture(resultHolder);
  }

  /**
   * Make request to the server to add country visit.
   *
   * @param tripId            Id of trip.
   * @param token             Jwt token.
   * @param addCountryRequest AddCountryRequest.
   * @return CompletableFuture of Result of String which holds result of request.
   */
  public CompletableFuture<Result<String>> addCountryVisit(Integer tripId, String token,
      AddCountryRequest addCountryRequest) {
    ResultHolder<String> resultHolder = new ResultHolder<>();
    Call<String> addCountryCall = tripService.addCountryVisit(tripId, addCountryRequest,
        getWrappedToken(token));
    addCountryCall.enqueue(getCallback(resultHolder, "Trip with this id not exist", (result) -> {
    }));
    return getCompletableFuture(resultHolder);
  }

  /**
   * Make request to the server to delete country visit.
   *
   * @param countryVisitId Id of trip.
   * @param token          Jwt token.
   * @return CompletableFuture of Result of String which holds result of request.
   */
  public CompletableFuture<Result<String>> deleteCountryVisit(Integer countryVisitId,
      String token) {
    ResultHolder<String> resultHolder = new ResultHolder<>();
    Call<String> deleteCountryCall = tripService.deleteCountryVisit(countryVisitId,
        getWrappedToken(token));
    deleteCountryCall.enqueue(
        getCallback(resultHolder, "Country with this id not exist", (result) -> {
        }));
    return getCompletableFuture(resultHolder);
  }

}