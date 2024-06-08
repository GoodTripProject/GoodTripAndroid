package ru.hse.goodtrip.ui.profile.followers;

import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import ru.hse.goodtrip.data.UsersRepository;
import ru.hse.goodtrip.data.model.User;

/**
 * FollowingViewModel.
 */
public class FollowingViewModel extends ViewModel {

  @Getter
  private List<User> users = new ArrayList<>(); // TODO: get following

  FollowingViewModel() {
    users.add(UsersRepository.getInstance().getLoggedUser());
  }

  public List<User> getFollowers() {
//// TODO: SHOULD BE: List<User> followers = UsersRepository.getInstance().getFollowers();
    return users;
  }

  public List<User> getFollowing() {
    //// TODO: SHOULD BE: List<User> followers = UsersRepository.getInstance().getFollowing();

    return users;
  }
}