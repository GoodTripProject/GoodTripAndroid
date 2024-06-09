package ru.hse.goodtrip.ui.profile.followers;

import androidx.lifecycle.ViewModel;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.Consumer;
import ru.hse.goodtrip.data.CommunicationRepository;
import ru.hse.goodtrip.data.UsersRepository;
import ru.hse.goodtrip.data.model.Result;
import ru.hse.goodtrip.data.model.User;

public class AddFollowingViewModel extends ViewModel {

  CommunicationRepository communicationRepository = CommunicationRepository.getInstance();

  public void findUser(String handleToFind, Consumer<User> workAfter) {
    communicationRepository.getUserByHandle(handleToFind,
            UsersRepository.getInstance().user.getToken())
        .thenApplyAsync((result) -> {
          if (result.isSuccess()) {
            ru.hse.goodtrip.network.social.entities.User networkUser =
                ((Result.Success<ru.hse.goodtrip.network.social.entities.User>) result).getData();
            try {
              return new User(networkUser.getId(), networkUser.getHandle(),
                  networkUser.getName() + " " + networkUser.getSurname(),
                  new URL(networkUser.getImageLink()), "");
            } catch (MalformedURLException ignored) {
            }
          }
          return null;
        })
        .thenAccept(workAfter);
  }
}