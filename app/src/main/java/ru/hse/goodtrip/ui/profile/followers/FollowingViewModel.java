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

  CommunicationRepository communicationRepository = CommunicationRepository.getInstance();
  @Getter
  @Setter
  private List<User> users = new ArrayList<>(); // TODO: get following
  @Setter
  private User user;

  public void updateUsers(Runnable uiUpdate) {
    communicationRepository.getFollowers(UsersRepository.getInstance().user.getId(),
            UsersRepository.getInstance().user.getToken())
        .thenAcceptAsync(
            (newUsers) -> {
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
        ).thenRunAsync(uiUpdate);
  }
}