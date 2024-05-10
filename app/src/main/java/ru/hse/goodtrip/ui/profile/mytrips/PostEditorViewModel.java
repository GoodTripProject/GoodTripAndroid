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

  public void postTrip() {
//    TripRepository.postTrip(trip);
    trip.setTripState(TripState.PUBLISHED);
    saveTrip();
    // TODO Add changing status of trip here
  }

  public void saveTrip() {
    // TODO
    tripRepository.updateTrip(
        TripRepository.getNetworkTripFromTrip(UsersRepository.getInstance().user.getId(), trip),
        UsersRepository.getInstance().user.getToken());
    /*for (CountryVisit visit : countries) {
      tripRepository.addCountryVisit(trip.getTripId(),
              UsersRepository.getInstance().user.getToken(),
              TripRepository.getAddCountryRequestFromCountryVisit(visit))
          .whenCompleteAsync((result, throwable) -> {
            Log.d(this.getClass().getSimpleName(),
                "Add country visit happened, result is:" + result);
            // TODO maybe add some logic
          });
    }
    for (Note note : notes) {
      tripRepository.addNote(UsersRepository.getInstance().user.getId(),
              UsersRepository.getInstance().user.getToken(),
              new AddNoteRequest(note.getHeadline(), note.getPhotoUrl(), note.getNote(),
                  note.getPlace().getName(), trip.getTripId()))
          .whenCompleteAsync((result, throwable) -> {
            Log.d(this.getClass().getName(), "Add note happened");
            //TODO maybe add some logic
          });
    }*/
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
    //TODO
    trip.getNotes().add(new Note(noteHeadline, noteText, photo,
        new City(place, new Coordinates(0, 0), new Country("", new Coordinates(0, 0)))));

  }
}