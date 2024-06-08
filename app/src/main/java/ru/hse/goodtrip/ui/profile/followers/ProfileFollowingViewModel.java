package ru.hse.goodtrip.ui.profile.followers;

import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;
import ru.hse.goodtrip.data.model.User;

@Getter
@Setter

public class ProfileFollowingViewModel extends ViewModel {

  private User user;
  private ArrayList<User> followers = new ArrayList<>();
  private ArrayList<User> following = new ArrayList<>();

  public void refreshFollow() {
    // updating followers and following
  }

  public int getTripsCount() {
    return 52;
  }
}