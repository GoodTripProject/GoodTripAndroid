package ru.hse.goodtrip.ui.map;

import androidx.lifecycle.ViewModel;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import lombok.Getter;
import ru.hse.goodtrip.data.TripRepository;
import ru.hse.goodtrip.data.UsersRepository;
import ru.hse.goodtrip.data.model.trips.Trip;

@Getter
public class MapsViewModel extends ViewModel {

  private List<Trip> marks = Collections.emptyList();


  public void refreshMarks() {
    try {
      TripRepository.getInstance().getUserTrips(UsersRepository.getInstance().user.getId(),
          UsersRepository.getInstance().user.getToken()).get();
    } catch (ExecutionException | InterruptedException ignored) {
    }
    marks = TripRepository.getInstance().getUserTrips();
  }
}
