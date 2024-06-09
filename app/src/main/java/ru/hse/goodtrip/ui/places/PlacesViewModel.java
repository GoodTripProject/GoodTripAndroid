package ru.hse.goodtrip.ui.places;

import androidx.lifecycle.ViewModel;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import ru.hse.goodtrip.data.PlacesRepository;
import ru.hse.goodtrip.data.UsersRepository;
import ru.hse.goodtrip.data.model.Result;
import ru.hse.goodtrip.network.places.model.PlaceResponse;

public class PlacesViewModel extends ViewModel {

  private final PlacesRepository repository = PlacesRepository.getInstance();
  @Getter
  private List<PlaceResponse> responses = Collections.emptyList();

  /**
   * Update places.
   *
   * @param latitude  latitude.
   * @param longitude longitude.
   * @param update    runnable to run after getting places.
   */
  public void updatePlaces(double latitude, double longitude, Runnable update) {
    repository.getPlacesNearby(latitude, longitude, 1000,
            null, null, UsersRepository.getInstance().user.getToken())
        .thenAcceptAsync(result -> {
          if (result.isSuccess()) {
            responses = ((Result.Success<List<PlaceResponse>>) result).getData();
          }
        }).thenRunAsync(update);
  }
}