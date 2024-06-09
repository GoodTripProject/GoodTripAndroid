package ru.hse.goodtrip.ui.profile.followers;

import androidx.lifecycle.ViewModel;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import ru.hse.goodtrip.data.CommunicationRepository;
import ru.hse.goodtrip.data.UsersRepository;
import ru.hse.goodtrip.data.model.Result;
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

  CommunicationRepository communicationRepository = CommunicationRepository.getInstance();
  @Getter
  @Setter
  private List<User> users = new ArrayList<>(); // TODO: get following

  public void updateFollowersUsers(Runnable uiUpdate) {
    communicationRepository.getFollowers(UsersRepository.getInstance().user.getId(),
            UsersRepository.getInstance().user.getToken())
        .thenAcceptAsync(
            this::updateUsers
        ).thenRunAsync(uiUpdate);
  }
  private void updateUsers(Result<List<ru.hse.goodtrip.network.social.entities.User>> newUsers){
    if (newUsers.isSuccess()) {
      users = ((Result.Success<List<ru.hse.goodtrip.network.social.entities.User>>) newUsers).getData()
          .stream().map(networkUser -> {
            try {
              return new User(networkUser.getId(), networkUser.getHandle(),
                  networkUser.getName() + " " + networkUser.getSurname(),
                  new URL(networkUser.getImageLink()), "");
            } catch (MalformedURLException ignored) {
            }
            return null;
          }).collect(Collectors.toList());
    }
  }
  public void updateFollowingUsers(Runnable uiUpdate) {
    communicationRepository.getSubscriptions(UsersRepository.getInstance().user.getId(),
            UsersRepository.getInstance().user.getToken())
        .thenAcceptAsync(
            this::updateUsers
        ).thenRunAsync(uiUpdate);
  }
}