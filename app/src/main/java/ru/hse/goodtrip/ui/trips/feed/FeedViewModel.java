package ru.hse.goodtrip.ui.trips.feed;

import android.os.Handler;
import android.os.Looper;
import androidx.lifecycle.ViewModel;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.Getter;
import ru.hse.goodtrip.data.TripRepository;
import ru.hse.goodtrip.data.model.Result;
import ru.hse.goodtrip.data.model.ResultHolder;
import ru.hse.goodtrip.data.model.User;
import ru.hse.goodtrip.data.model.trips.City;
import ru.hse.goodtrip.data.model.trips.Coordinates;
import ru.hse.goodtrip.data.model.trips.Country;
import ru.hse.goodtrip.data.model.trips.CountryVisit;
import ru.hse.goodtrip.data.model.trips.Note;
import ru.hse.goodtrip.data.model.trips.Trip;

@Getter
public class FeedViewModel extends ViewModel {

  public static User fakeUser = new User(0, "aboba", "Jane Doe", null, null);
  private final TripRepository tripRepository = TripRepository.getInstance();
  @Getter
  private List<Trip> posts = Collections.emptyList();

  /**
   * Initialize FeedViewModel.
   */
  public FeedViewModel() {
    posts = new ArrayList<>();
  }

  private void runExecutorToWaitResult(ResultHolder<List<Trip>> result, Runnable func) {
    ExecutorService executor = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());
    executor.execute(() -> {
      synchronized (result) {
        try {
          result.wait();
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
      }
      handler.post(func);
    });

  }

  public void getUserTrips(Integer userId, String token) {
    ResultHolder<List<Trip>> result = tripRepository.getUserTrips(userId, token);
    runExecutorToWaitResult(result, () -> {
      synchronized (result) {
        if (result.getResult().isSuccess()) {
          posts = ((Result.Success<List<Trip>>) result.getResult()).getData();
          synchronized (FeedViewModel.class) {
            FeedViewModel.class.notify();
          }
        }
      }
    });
  }

  public void newPostPublished(Trip post) {
  }
}