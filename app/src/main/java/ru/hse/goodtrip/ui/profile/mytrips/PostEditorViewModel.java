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


  public List<String> getCountries() {
    return new ArrayList<>(); //TODO
  }
}