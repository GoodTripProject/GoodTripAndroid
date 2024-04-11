package ru.hse.goodtrip.ui.map;

import androidx.lifecycle.ViewModel;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import ru.hse.goodtrip.data.TripRepository;
import ru.hse.goodtrip.data.model.trips.Trip;

public class MapsViewModel extends ViewModel {

  @Getter
  private List<Trip> marks = Collections.emptyList();


  public void refreshMarks() {
    marks = TripRepository.getInstance().getTrips();
  }
}
