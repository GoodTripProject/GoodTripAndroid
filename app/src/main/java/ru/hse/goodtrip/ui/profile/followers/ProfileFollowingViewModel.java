package ru.hse.goodtrip.ui.profile.followers;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import ru.hse.goodtrip.data.CommunicationRepository;
import ru.hse.goodtrip.data.UsersRepository;
import ru.hse.goodtrip.data.model.Result.Success;
import ru.hse.goodtrip.data.model.User;

/**
 * ProfileFollowingViewModel.
 */
@Getter
@Setter
public class ProfileFollowingViewModel extends ViewModel {

  CommunicationRepository communicationRepository = CommunicationRepository.getInstance();
  private User user;
  private ArrayList<User> followers = new ArrayList<>();
  private ArrayList<User> following = new ArrayList<>();

  @NonNull
  private static ArrayList<User> getResult(
      Success<List<ru.hse.goodtrip.network.social.entities.User>> newUsers) {
    return newUsers.getData().stream().map(networkUser -> {
      try {
        URL linkToPhoto = null;
        if (networkUser.getImageLink() != null) {
          linkToPhoto = new URL(networkUser.getImageLink());
        }
        return new User(networkUser.getId(), networkUser.getHandle(),
            networkUser.getName() + " " + networkUser.getSurname(),
            linkToPhoto, "");
      } catch (MalformedURLException e) {
        Log.e(ProfileFollowingViewModel.class.getSimpleName(),
            Objects.requireNonNull(e.getLocalizedMessage()));
      }
      return null;
    }).collect(Collectors.toCollection(ArrayList::new));
  }

  /**
   * Refresh followings of user.
   *
   * @param uiUpdate Ui update Runnable.
   */
  public void refreshFollow(Runnable uiUpdate) {
    communicationRepository.getFollowers(user.getId(),
            UsersRepository.getInstance().user.getToken())
        .thenAccept((newUsers) -> {
          if (newUsers.isSuccess()) {
            followers = getResult(
                (Success<List<ru.hse.goodtrip.network.social.entities.User>>) newUsers);
          }
        }).thenRunAsync(uiUpdate);
    communicationRepository.getSubscriptions(user.getId(),
            UsersRepository.getInstance().user.getToken())
        .thenAccept((newUsers) -> {
          if (newUsers.isSuccess()) {
            following = getResult(
                (Success<List<ru.hse.goodtrip.network.social.entities.User>>) newUsers);
          }
        });

  }

  /**
   * Follow user and run network call.
   *
   * @param user requested User.
   */
  public void follow(User user) {
    communicationRepository.follow(
        UsersRepository.getInstance().user.getId(),
        user.getHandle(),
        UsersRepository.getInstance().user.getToken());
  }

  /**
   * Unfollow user and run network call.
   *
   * @param user requested User.
   */
  public void unfollow(User user) {
    communicationRepository.unfollow(
        UsersRepository.getInstance().user.getId(),
        user.getHandle(),
        UsersRepository.getInstance().user.getToken());
  }
}