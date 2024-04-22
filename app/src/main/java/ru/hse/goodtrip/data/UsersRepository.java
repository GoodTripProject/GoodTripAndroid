package ru.hse.goodtrip.data;

import android.net.Uri;
import androidx.annotation.NonNull;
import java.util.concurrent.CompletableFuture;
import lombok.Setter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.hse.goodtrip.data.model.Result;
import ru.hse.goodtrip.data.model.Result.Error;
import ru.hse.goodtrip.data.model.Result.Success;
import ru.hse.goodtrip.data.model.ResultHolder;
import ru.hse.goodtrip.data.model.User;
import ru.hse.goodtrip.network.NetworkManager;
import ru.hse.goodtrip.network.authentication.LoginService;
import ru.hse.goodtrip.network.authentication.model.AuthenticationResponse;
import ru.hse.goodtrip.network.authentication.model.AuthorizationRequest;
import ru.hse.goodtrip.network.authentication.model.RegisterRequest;

/**
 * Class that requests authentication and user information from the remote data source and maintains
 * an in-memory cache of login status and user credentials information.
 */
public class UsersRepository extends AbstractRepository {

  private static volatile UsersRepository instance;

  private final LoginService loginService;

  public User user = null;
  @Setter
  private String userToken;


  private UsersRepository() {
    super();
    this.loginService = NetworkManager.getInstance().getInstanceOfService(LoginService.class);

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

  public static void changeUserMainPhoto(Uri newPhoto) {

  }

  public User getLoggedUser() {
    return user;
  }

  public boolean isLoggedIn() {
    return user != null;
  }

  public void logout() {
    //TODO
  }

  /**
   * set logged in user.
   *
   * @param user LoggedInUser user.
   */
  private void setLoggedInUser(User user) {
    this.user = user;
  }

  /**
   * login user.
   *
   * @param username user name.
   * @param password password.
   * @return result value.
   */
  public CompletableFuture<Result<User>> login(String username, String password) {
    final ResultHolder<User> resultOfAuthorization = new ResultHolder<>();
    Call<AuthenticationResponse> loginServiceCall = loginService.login(
        new AuthorizationRequest(username, password));
    loginServiceCall.enqueue(
        getCallback(resultOfAuthorization, "Username or password are not correct"
        ));
    return super.getCompletableFuture(resultOfAuthorization);
  }

  @NonNull
  private Callback<AuthenticationResponse> getCallback(ResultHolder<User> resultOfAuthorization,
      String failureAuthenticationString) {
    return new Callback<AuthenticationResponse>() {
      @Override
      public void onResponse(@NonNull Call<AuthenticationResponse> call,
          @NonNull Response<AuthenticationResponse> response) {
        AuthenticationResponse authenticationResponse = response.body();
        if (authenticationResponse == null) {
          resultOfAuthorization.setResult(
              new Error<>(new InterruptedException(failureAuthenticationString)));
        } else {
          setLoggedInUser(
              new User(authenticationResponse.getId(), authenticationResponse.getHandle(),
                  authenticationResponse.getName() + " " + authenticationResponse.getSurname(),
                  authenticationResponse.getUrl(), authenticationResponse.getToken()));
          resultOfAuthorization.setResult(new Success<>(user));
        }
      }

      @Override
      public void onFailure(@NonNull Call<AuthenticationResponse> call,
          @NonNull Throwable throwable) {
        resultOfAuthorization.setResult(
            new Error<>(new InterruptedException("Some connection issues happened")));
      }
    };
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
  public CompletableFuture<Result<User>> signUp(String username, String password, String handle,
      String name,
      String surname) {
    final ResultHolder<User> resultOfAuthorization = new ResultHolder<>();
    Call<AuthenticationResponse> loginServiceCall = loginService.register(
        new RegisterRequest(username, handle, password, name, surname));
    loginServiceCall.enqueue(
        getCallback(resultOfAuthorization, "Username is not correct or is already taken"
        ));
    return super.getCompletableFuture(resultOfAuthorization);
  }
}