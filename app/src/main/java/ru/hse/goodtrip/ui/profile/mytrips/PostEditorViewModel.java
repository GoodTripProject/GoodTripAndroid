package ru.hse.goodtrip.ui.profile.mytrips;

import android.net.Uri;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import ru.hse.goodtrip.data.TripRepository;
import ru.hse.goodtrip.data.model.trips.City;
import ru.hse.goodtrip.data.model.trips.Coordinates;
import ru.hse.goodtrip.data.model.trips.Country;
import ru.hse.goodtrip.data.model.trips.CountryVisit;
import ru.hse.goodtrip.data.model.trips.Note;
import ru.hse.goodtrip.data.model.trips.Trip;

/**
 * PostEditorViewModel.
 */
@Getter
@Setter
public class PostEditorViewModel extends ViewModel {

  Trip trip;
  List<CountryVisit> countries = new ArrayList<>();
  List<Note> notes = new ArrayList<>();

  String photo;

  public void postTrip(Trip trip) {
//    TripRepository.postTrip(trip);
  }

  public void saveTrip() {
    // TODO
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
    countries.add(countryVisit);
  }

  public void addNote(String noteHeadline, String noteText, String place, String photo) {

  }
}