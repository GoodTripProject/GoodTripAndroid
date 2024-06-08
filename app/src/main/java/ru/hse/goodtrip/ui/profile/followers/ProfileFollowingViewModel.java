package ru.hse.goodtrip.ui.profile.followers;

import androidx.lifecycle.ViewModel;
import lombok.Getter;
import lombok.Setter;
import ru.hse.goodtrip.data.model.User;

public class ProfileFollowingViewModel extends ViewModel {

  @Getter
  @Setter
  private User user;
}