package ru.hse.goodtrip.ui.trips.feed.post;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import lombok.Getter;
import lombok.Setter;
import ru.hse.goodtrip.data.model.trips.Trip;

@Getter
@Setter
public class PostViewModel extends ViewModel {

  private MutableLiveData<Trip> trip = new MutableLiveData<>();

  public void setTrip(Trip trip) {
    this.trip = new MutableLiveData<>(trip);
  }
}
