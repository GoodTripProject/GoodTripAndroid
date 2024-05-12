package ru.hse.goodtrip.ui.profile.mytrips;


import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import ru.hse.goodtrip.data.TripRepository;
import ru.hse.goodtrip.data.UsersRepository;
import ru.hse.goodtrip.data.model.trips.City;
import ru.hse.goodtrip.data.model.trips.Coordinates;
import ru.hse.goodtrip.data.model.trips.Country;
import ru.hse.goodtrip.data.model.trips.CountryVisit;
import ru.hse.goodtrip.data.model.trips.Note;
import ru.hse.goodtrip.data.model.trips.Trip;
import ru.hse.goodtrip.network.trips.model.TripState;


/**
 * PostEditorViewModel.
 */
@Getter
@Setter
public class PostEditorViewModel extends ViewModel {

  private TripRepository tripRepository = TripRepository.getInstance();
  private Trip trip;

  String photo;

  /**
   * Post and save trip.
   */
  public void postTrip() {
    trip.setTripState(TripState.PUBLISHED);
    saveTrip();
  }

  /**
   * Save trip.
   */
  public void saveTrip() {
    int userId = UsersRepository.getInstance().user.getId();
    String token = UsersRepository.getInstance().user.getToken();
    tripRepository.updateTrip(TripRepository.getNetworkTripFromTrip(userId, trip), token)
        .thenRunAsync(() -> tripRepository.getUserTrips(userId, token))
        .thenRunAsync(() -> tripRepository.getAuthorsTrips(userId, token));
  }

  /**
   * add country to trip.
   *
   * @param countryName country name.
   * @param citiesName  name of cities.
   */
  public void addCountry(String countryName, List<String> citiesName) {
    Country country = new Country(countryName, new Coordinates(0, 0));
    List<City> cities = new ArrayList<>();
    for (String cityName : citiesName) {
      cities.add(new City(cityName, new Coordinates(0, 0), country));
    }
    CountryVisit countryVisit = new CountryVisit(country, cities);
    trip.getCountries().add(countryVisit);
  }

  public void addNote(String noteHeadline, String noteText, String place, String photo) {
    trip.getNotes().add(new Note(noteHeadline, noteText, photo,
        new City(place, new Coordinates(0, 0), new Country("", new Coordinates(0, 0)))));

  }
}