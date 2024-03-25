package ru.hse.goodtrip.ui.trips.feed;

import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
import ru.hse.goodtrip.data.TripRepository;
import ru.hse.goodtrip.data.model.trips.Trip;

public class FeedViewModel extends ViewModel {

  private ArrayList<Trip> posts = new ArrayList<>();

  /**
   * Initialize FeedViewModel.
   */
  public FeedViewModel() {
    setPosts(TripRepository.getTripsOfUser());
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