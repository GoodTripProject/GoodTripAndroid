package ru.hse.goodtrip.ui.profile.followers;

import android.util.Log;
import androidx.lifecycle.ViewModel;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.function.Consumer;
import ru.hse.goodtrip.data.CommunicationRepository;
import ru.hse.goodtrip.data.UsersRepository;
import ru.hse.goodtrip.data.model.Result;
import ru.hse.goodtrip.data.model.User;

/**
 * AddFollowingViewModel.
 */
public class AddFollowingViewModel extends ViewModel {

  CommunicationRepository communicationRepository = CommunicationRepository.getInstance();

  /**
   * Find user by handle.
   *
   * @param handleToFind handle of User, which is finding.
   * @param workAfter    function, which accepts User after getting callback.
   */
  public void findUser(String handleToFind, Consumer<User> workAfter) {
    communicationRepository.getUserByHandle(handleToFind,
            UsersRepository.getInstance().user.getToken())
        .thenApplyAsync((result) -> {
          if (result.isSuccess()) {
            ru.hse.goodtrip.network.social.entities.User networkUser =
                ((Result.Success<ru.hse.goodtrip.network.social.entities.User>) result).getData();
            try {
              URL linkToPhoto = null;
              if (networkUser.getImageLink() != null) {
                linkToPhoto = new URL(networkUser.getImageLink());
              }
              return new User(networkUser.getId(), networkUser.getHandle(),
                  networkUser.getName() + " " + networkUser.getSurname(),
                  linkToPhoto, "");
            } catch (MalformedURLException e) {
              Log.d(getClass().getSimpleName(),
                  Objects.requireNonNull(e.getLocalizedMessage()));
            }
          }
          return null;
        })
        .thenAccept(workAfter);
  }
}