package ru.hse.goodtrip.data;

import static java.util.stream.Collectors.toCollection;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import retrofit2.Call;
import ru.hse.goodtrip.data.model.Result;
import ru.hse.goodtrip.data.model.ResultHolder;
import ru.hse.goodtrip.data.model.User;
import ru.hse.goodtrip.network.NetworkManager;
import ru.hse.goodtrip.network.authentication.LoginService;
import ru.hse.goodtrip.network.authentication.model.AuthenticationResponse;
import ru.hse.goodtrip.network.authentication.model.AuthorizationRequest;
import ru.hse.goodtrip.network.authentication.model.RegisterRequest;
import ru.hse.goodtrip.network.authentication.model.UrlHandler;
import ru.hse.goodtrip.network.social.CommunicationService;

/**
 * Class that requests authentication and user information from the remote data source and maintains
 * an in-memory cache of login status and user credentials information.
 */
public class UsersRepository extends AbstractRepository {

  private static volatile UsersRepository instance;

  private final LoginService loginService;

  private final CommunicationService communicationService;

  public User user = null;

  @Getter
  private ArrayList<User> followers = new ArrayList<>(); // TODO

  @Getter
  private ArrayList<User> following = new ArrayList<>(); // TODO

  private UsersRepository() {
    super();
    this.loginService = NetworkManager.getInstance().getInstanceOfService(LoginService.class);
    this.communicationService = NetworkManager.getInstance()
        .getInstanceOfService(CommunicationService.class);

  }

  /**
   * Get instance of Login Repository.
   *
   * @return login repository.
   */
  public static UsersRepository getInstance() {
    if (instance == null) {
      instance = new UsersRepository();
    }
    return instance;
  }


  public User getLoggedUser() {
    return user;
  }


  public void logout() {
    user = null;
  }

  /**
   * set logged in user.
   *
   * @param user LoggedInUser user.
   */
  private void setLoggedInUser(User user) {
    this.user = user;
  }

  private User getUserFromNetworkUser(ru.hse.goodtrip.network.social.entities.User user) {
    try {
      return new User(user.getId(), user.getHandle(),
          user.getName() + " " + user.getSurname(),
          new URL(user.getImageLink()), "");
    } catch (MalformedURLException ignored) {
    }
    return null;
  }

  private void updateFollowersAndFollowing() {
    Call<List<ru.hse.goodtrip.network.social.entities.User>>
        getFollowersCall = communicationService.getFollowers(user.getId(),
        getWrappedToken(user.getToken()));
    final ResultHolder<List<ru.hse.goodtrip.network.social.entities.User>> resultOfGetFollowers = new ResultHolder<>();
    getFollowersCall.enqueue(getCallback(resultOfGetFollowers,
        "Cannot get followers"
        , (result) -> this.followers = result.stream().map(this::getUserFromNetworkUser)
            .collect(toCollection(ArrayList::new))));
    Call<List<ru.hse.goodtrip.network.social.entities.User>>
        getSubscriptions = communicationService.getSubscriptions(user.getId(),
        getWrappedToken(user.getToken()));
    final ResultHolder<List<ru.hse.goodtrip.network.social.entities.User>>
        resultOfGetSubscriptions = new ResultHolder<>();
    getSubscriptions.enqueue(getCallback(resultOfGetSubscriptions,
        "Cannot get subscriptions"
        , (result) -> this.following = result.stream().map(this::getUserFromNetworkUser)
            .collect(toCollection(ArrayList::new))));
  }

  private void updatingToken(String username, String password) {
    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    executor.scheduleAtFixedRate(() -> {
      synchronized (this) {
        login(username, password);
      }
    }, 4, 4, TimeUnit.MINUTES);
    executor.shutdown();
  }

  /**
   * login user.
   *
   * @param username user name.
   * @param password password.
   * @return result value.
   */
  public CompletableFuture<Result<AuthenticationResponse>> login(String username, String password) {
    final ResultHolder<AuthenticationResponse> resultOfAuthorization = new ResultHolder<>();
    Call<AuthenticationResponse> loginServiceCall = loginService.login(
        new AuthorizationRequest(username, password));
    loginServiceCall.enqueue(
        getCallback(resultOfAuthorization, "Username or password are not correct"
            , (result) -> setLoggedInUser(
                new User(result.getId(), result.getHandle(),
                    result.getName() + " " + result.getSurname(),
                    result.getUrl(), result.getToken()))));
    updatingToken(username, password);
    return getCompletableFuture(resultOfAuthorization).whenCompleteAsync((result, throwable) -> {
      if (result.isSuccess()) {
        updatingToken(username, password);
        updateFollowersAndFollowing();
      }
    });
  }

  /**
   * Update photo.
   *
   * @param userId User id.
   * @param uri    Uri of photo.
   * @param token  Jwt token.
   */
  public void updatePhoto(int userId, String uri, String token) {
    final ResultHolder<String> resultOfUpdatingPhoto = new ResultHolder<>();
    Call<String> loginServiceCall = loginService.updateUserPhoto(userId, new UrlHandler(uri),
        getWrappedToken(token));
    loginServiceCall.enqueue(
        getCallback(resultOfUpdatingPhoto, "Updating photo failed",
            (result) -> {
            }));
  }

  /**
   * Sign up user.
   *
   * @param username username.
   * @param password password.
   * @param handle   handle.
   * @param name     user name.
   * @param surname  user surname.
   * @return result value.
   */
  public CompletableFuture<Result<AuthenticationResponse>> signUp(String username, String password,
      String handle,
      String name,
      String surname) {
    final ResultHolder<AuthenticationResponse> resultOfAuthorization = new ResultHolder<>();
    Call<AuthenticationResponse> loginServiceCall = loginService.register(
        new RegisterRequest(username, handle, password, name, surname));
    loginServiceCall.enqueue(
        getCallback(resultOfAuthorization, "Username is not correct or is already taken"
            , authenticationResponse -> setLoggedInUser(
                new User(authenticationResponse.getId(), authenticationResponse.getHandle(),
                    authenticationResponse.getName() + " " + authenticationResponse.getSurname(),
                    authenticationResponse.getUrl(), authenticationResponse.getToken()))));
    return getCompletableFuture(resultOfAuthorization).whenCompleteAsync((result, throwable) -> {
      if (result.isSuccess()) {
        updatingToken(username, password);
        updateFollowersAndFollowing();
      }
    });


  }
}