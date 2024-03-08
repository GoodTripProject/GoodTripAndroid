package ru.hse.goodtrip.ui.feed;

import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
import ru.hse.goodtrip.model.User;

public class FeedViewModel extends ViewModel {

  private ArrayList<PostTrip> posts = new ArrayList<>();

  /**
   * Initialize FeedViewModel.
   */
  public FeedViewModel() {
    PostTrip testPost = new PostTrip();
    User testUser = new User();
    testUser.name = "Danya Neykov";
    testPost.title = "Weekend in Heaven";
    testPost.user = testUser;
    testPost.dateArrival = "yesterday";
    for (int i = 0; i < 10; i++) {
      posts.add(testPost);
    }
  }

  public ArrayList<PostTrip> getPosts() {
    return posts;
  }

  public void setPosts(ArrayList<PostTrip> newPosts) {
    posts = newPosts;
  }

  public void newPostPublished(PostTrip post) {
    posts.add(post);
  }
}