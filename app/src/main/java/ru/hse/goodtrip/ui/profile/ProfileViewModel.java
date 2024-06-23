package ru.hse.goodtrip.ui.profile;

import androidx.lifecycle.ViewModel;
import lombok.Getter;
import ru.hse.goodtrip.data.UsersRepository;
import ru.hse.goodtrip.data.model.User;

/**
 * ProfileViewModel that provides user from repository.
 */
@Getter
public class ProfileViewModel extends ViewModel {

  private final User user = UsersRepository.getInstance().getLoggedUser();
}