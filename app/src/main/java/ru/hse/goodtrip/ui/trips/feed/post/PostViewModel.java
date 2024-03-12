package ru.hse.goodtrip.ui.trips.feed.post;

import androidx.lifecycle.ViewModel;
import ru.hse.goodtrip.data.model.trips.Trip;

public class PostViewModel extends ViewModel {

  private final Trip trip;

  public PostViewModel(Trip trip) {
    this.trip = trip;
  }

}
