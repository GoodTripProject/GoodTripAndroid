package ru.hse.goodtrip.ui.map;

import androidx.lifecycle.ViewModel;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import ru.hse.goodtrip.data.TripRepository;
import ru.hse.goodtrip.data.model.User;
import ru.hse.goodtrip.data.model.trips.Trip;

@Getter
@Setter
public class MapsFollowingViewModel extends ViewModel {

  private List<Trip> marks = Collections.emptyList();
  private User user;

  public void refreshMarks() {
    marks = TripRepository.getInstance().getUserTrips();

    // TODO: get trips of user
//    try {
//      TripRepository.getInstance().getUserTrips(UsersRepository.getInstance().user.getId(),
//          UsersRepository.getInstance().user.getToken()).get();
//    } catch (ExecutionException | InterruptedException ignored) {
//    }
//    marks = TripRepository.getInstance().getUserTrips();
  }
}
