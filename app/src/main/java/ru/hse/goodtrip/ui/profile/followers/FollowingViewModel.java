package ru.hse.goodtrip.ui.profile.followers;

import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import ru.hse.goodtrip.data.UsersRepository;
import ru.hse.goodtrip.data.model.User;

/**
 * FollowingViewModel.
 */
public class FollowingViewModel extends ViewModel {

  @Getter
  @Setter
  private List<User> users = new ArrayList<>(); // TODO: get following
  @Setter
  private User user;

  FollowingViewModel() {
    users.add(UsersRepository.getInstance().getLoggedUser());
  }
}