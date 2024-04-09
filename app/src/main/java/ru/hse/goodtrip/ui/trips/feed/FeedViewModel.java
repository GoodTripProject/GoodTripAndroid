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
import ru.hse.goodtrip.data.model.trips.City;
import ru.hse.goodtrip.data.model.trips.Coordinates;
import ru.hse.goodtrip.data.model.trips.Country;
import ru.hse.goodtrip.data.model.trips.CountryVisit;
import ru.hse.goodtrip.data.model.trips.Note;
import ru.hse.goodtrip.data.model.trips.Trip;

@Getter
public class FeedViewModel extends ViewModel {

  private final TripRepository tripRepository = TripRepository.getInstance();
  @Getter
  private List<Trip> posts = Collections.emptyList();

  /**
   * Initialize FeedViewModel.
   */
  public FeedViewModel() {
    posts = getFakeTrips();
  }

  private ArrayList<Trip> getFakeTrips() {
    Trip testTrip = new Trip("Weekend in Heaven", new ArrayList<>(), LocalDate.now(),
        LocalDate.now(), null,
        1000, new HashSet<>(), AuthService.fakeUser);
    Coordinates fakeCords = new Coordinates(-34, 131);
    Country fakeCountry = new Country("Russia", fakeCords);
    City fakeCity1 = new City("Moscow", fakeCords, fakeCountry);
    City fakeCity2 = new City("Saint-Petersburg", fakeCords, fakeCountry);
    ArrayList<City> cities = new ArrayList<>();
    cities.add(fakeCity1);
    cities.add(fakeCity2);
    CountryVisit countryVisit = new CountryVisit(fakeCountry, cities);
    ArrayList<CountryVisit> countryVisits = new ArrayList<>();
    countryVisits.add(countryVisit);
    testTrip.setCountries(countryVisits);
    final String note1 =
        "Что вершит судьбу человечества в этом мире?\n"
            + " Некое незримое существо или закон, подобно Длани Господней парящей над миром\n?"
            + " По крайне мере истинно то, что человек не властен даже над своей волей.\n";

    final String note2 = "Я видел такое, что вам, людям, и не снилось.\n"
        + " Атакующие корабли, пылающие над Орионом; Лучи Си, разрезающие мрак у ворот Тангейзера. \n"
        + "Все эти мгновения затеряются во времени, как... слёзы в дожде... Пришло время умирать.\n";

    Note firstNote = new Note("First note", note1, null, fakeCity1);
    Note secondNote = new Note("Second note", note2, null, fakeCity2);
    List<Note> notes = new ArrayList<>();
    notes.add(firstNote);
    notes.add(secondNote);
    testTrip.setNotes(notes);
    ArrayList<Trip> fakeTrips = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      fakeTrips.add(testTrip);
    }
    return fakeTrips;
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