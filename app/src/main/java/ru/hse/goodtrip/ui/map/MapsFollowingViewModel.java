package ru.hse.goodtrip.ui.map;

import androidx.lifecycle.ViewModel;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import lombok.Getter;
import lombok.Setter;
import ru.hse.goodtrip.data.TripRepository;
import ru.hse.goodtrip.data.UsersRepository;
import ru.hse.goodtrip.data.model.Result;
import ru.hse.goodtrip.data.model.User;
import ru.hse.goodtrip.data.model.trips.Trip;

/**
 * MapsFollowingViewModel.
 */
@Getter
@Setter
public class MapsFollowingViewModel extends ViewModel {

  private List<Trip> marks = Collections.emptyList();
  private User user;
  private TripRepository tripRepository = TripRepository.getInstance();

  public void refreshMarks() {
    try {
      Result<List<ru.hse.goodtrip.network.trips.model.Trip>> trips = tripRepository.getAuthorTrips(
          user.getHandle(),
          UsersRepository.getInstance().user.getToken()).get();
      if (trips.isSuccess()) {
        marks = TripRepository.getTripsFromTripResponses(
            ((Result.Success<List<ru.hse.goodtrip.network.trips.model.Trip>>) trips).getData());
      }
    } catch (ExecutionException | InterruptedException ignored) {
    }

  }
}
