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
import ru.hse.goodtrip.data.model.User;

@Getter
@Setter
public class ProfileFollowingViewModel extends ViewModel {

  CommunicationRepository communicationRepository = CommunicationRepository.getInstance();
  private User user;
  private ArrayList<User> followers = new ArrayList<>();
  private ArrayList<User> following = new ArrayList<>();

  public void refreshFollow(Runnable uiUpdate) {
    communicationRepository.getFollowers(user.getId(),
        UsersRepository.getInstance().user.getToken()).thenAccept((newUsers) -> {
      if (newUsers.isSuccess()) {
        followers = ((Result.Success<List<ru.hse.goodtrip.network.social.entities.User>>) newUsers).getData()
            .stream().map(networkUser -> {
              try {
                return new User(networkUser.getId(), networkUser.getHandle(),
                    networkUser.getName() + " " + networkUser.getSurname(),
                    new URL(networkUser.getImageLink()), "");
              } catch (MalformedURLException ignored) {
              }
              return null;
            }).collect(Collectors.toCollection(ArrayList::new));
      }
    }).thenRunAsync(uiUpdate);
    communicationRepository.getSubscriptions(user.getId(),
        UsersRepository.getInstance().user.getToken()).thenAccept((newUsers) -> {
      if (newUsers.isSuccess()) {
        following = ((Result.Success<List<ru.hse.goodtrip.network.social.entities.User>>) newUsers).getData()
            .stream().map(networkUser -> {
              try {
                return new User(networkUser.getId(), networkUser.getHandle(),
                    networkUser.getName() + " " + networkUser.getSurname(),
                    new URL(networkUser.getImageLink()), "");
              } catch (MalformedURLException ignored) {
              }
              return null;
            }).collect(Collectors.toCollection(ArrayList::new));
      }
    });

  }

  public void follow(User user) {
    communicationRepository.follow(UsersRepository.getInstance().user.getId(),
        user.getHandle(), UsersRepository.getInstance().user.getToken());
  }

  public void unfollow(User user) {
    communicationRepository.unfollow(UsersRepository.getInstance().user.getId(),
        user.getHandle(), UsersRepository.getInstance().user.getToken());
  }
}