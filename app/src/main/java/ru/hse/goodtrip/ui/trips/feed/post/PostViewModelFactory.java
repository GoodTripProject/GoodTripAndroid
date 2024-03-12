package ru.hse.goodtrip.ui.trips.feed.post;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import ru.hse.goodtrip.data.model.trips.Trip;

public class PostViewModelFactory implements ViewModelProvider.Factory {

  private final Trip trip;

  public PostViewModelFactory(Trip trip) {
    this.trip = trip;
  }

  @NonNull
  @Override
  public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
    if (modelClass.isAssignableFrom(PostViewModel.class)) {
      return (T) new PostViewModel(trip);
    }
    throw new IllegalArgumentException("Unknown ViewModel class");
  }
}
