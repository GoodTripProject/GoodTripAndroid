package ru.hse.goodtrip.ui.profile.following;

import androidx.lifecycle.ViewModel;
import ru.hse.goodtrip.data.UsersRepository;
import ru.hse.goodtrip.data.model.User;

public class AddFollowingViewModel extends ViewModel {

  public User findUser(String handleToFind) {
    // TODO

    return UsersRepository.getInstance().getLoggedUser();
  }
}