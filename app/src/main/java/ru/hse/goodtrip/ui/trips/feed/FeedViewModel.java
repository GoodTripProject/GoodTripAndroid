package ru.hse.goodtrip.ui.trips.feed;

import android.util.Log;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import lombok.Getter;
import ru.hse.goodtrip.data.TripRepository;
import ru.hse.goodtrip.data.model.Result;
import ru.hse.goodtrip.network.trips.model.TripView;

/**
 * ViewModel that provides posts to Feed.
 */
@Getter
public class FeedViewModel extends ViewModel {

  private final TripRepository tripRepository = TripRepository.getInstance();
  @Getter
  private List<TripView> posts;

  /**
   * Initialize FeedViewModel.
   */
  public FeedViewModel() {
    posts = new ArrayList<>();
  }

  /**
   * Get all trips of authors.
   *
   * @param userId user Id.
   * @param token  users token.
   */
  public void getAuthorTrips(Integer userId, String token) {
    CompletableFuture<Result<List<
        ru.hse.goodtrip.network.trips.model.TripView>>> future =
        tripRepository.getAuthorsTrips(
            userId, token);
    Log.d(FeedViewModel.class.getName(),
        "Completable future is accepted");
    try {
      future.whenCompleteAsync((result, exception) -> {
        Log.d(FeedViewModel.class.getName(),
            "Async handling of result is happening: " + result + " exception: " + exception);
        if (result.isSuccess()) {
          posts = ((Result.Success<List<TripView>>) result)
              .getData();
        }
      }).get();
    } catch (InterruptedException e) {
      Log.e(this.getClass().getName(), "InterruptedException happened in trips getting: " + e);
    } catch (ExecutionException e) {
      Log.e(this.getClass().getName(), "ExecutionException happened in trips getting: " + e);
    }
  }
}