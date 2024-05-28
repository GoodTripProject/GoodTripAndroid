package ru.hse.goodtrip.ui.profile.following;

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
}