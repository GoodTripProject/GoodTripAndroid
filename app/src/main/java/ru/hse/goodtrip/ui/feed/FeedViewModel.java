package ru.hse.goodtrip.ui.feed;

import androidx.lifecycle.ViewModel;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import ru.hse.goodtrip.data.LoginService;
import ru.hse.goodtrip.data.model.trips.Trip;

public class FeedViewModel extends ViewModel {

  // Trip for testing UI
  public static Trip testTrip = new Trip("Weekend in Heaven", new ArrayList<>(), LocalDate.now(),
      LocalDate.now(), null,
      1000, new HashSet<>(), LoginService.fakeUser);
  private ArrayList<Trip> posts = new ArrayList<>();

  /**
   * Initialize FeedViewModel.
   */
  public FeedViewModel() {
    for (int i = 0; i < 10; i++) {
      posts.add(testTrip);
    }
  }

  public ArrayList<Trip> getPosts() {
    return posts;
  }

  public void setPosts(ArrayList<Trip> newPosts) {
    posts = newPosts;
  }

  public void newPostPublished(Trip post) {
    posts.add(0, post);
  }
}