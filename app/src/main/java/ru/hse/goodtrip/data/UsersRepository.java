package ru.hse.goodtrip.data;

import androidx.annotation.NonNull;
import lombok.Setter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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
public class UsersRepository {

  public static final User fakeUser = new User(
      "Jane Doe", null, "aboba");

  private static volatile UsersRepository instance;

  private final LoginService loginService;

  public User user = null;
  @Setter
  private String userToken;


  private UsersRepository() {
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
  public ResultHolder<User> login(String username, String password) {
    final ResultHolder<User> resultOfAuthorization = new ResultHolder<>();
    Call<AuthenticationResponse> loginServiceCall = loginService.login(
        new AuthorizationRequest(username, password));
    loginServiceCall.enqueue(
        getCallback(resultOfAuthorization, "Username or password are not correct",
            "Some connection issues happened"));
    return resultOfAuthorization;
  }

  @NonNull
  private Callback<AuthenticationResponse> getCallback(ResultHolder<User> resultOfAuthorization,
      String failureAuthenticationString, String failureConnectionString) {
    return new Callback<AuthenticationResponse>() {
      @Override
      public void onResponse(@NonNull Call<AuthenticationResponse> call,
          @NonNull Response<AuthenticationResponse> response) {
        synchronized (resultOfAuthorization) {
          AuthenticationResponse authenticationResponse = response.body();
          if (authenticationResponse == null) {
            resultOfAuthorization.setResult(
                new Error<>(new InterruptedException(failureAuthenticationString)));
          } else {
            userToken = authenticationResponse.getToken();
            setLoggedInUser(new User(
                authenticationResponse.getName() + " " + authenticationResponse.getSurname(),
                authenticationResponse.getUrl().toString(), "aboba"));
            resultOfAuthorization.setResult(new Success<>(user));
          }
          resultOfAuthorization.notify();
        }
      }

      @Override
      public void onFailure(@NonNull Call<AuthenticationResponse> call,
          @NonNull Throwable throwable) {
        synchronized (resultOfAuthorization) {
          resultOfAuthorization.setResult(
              new Error<>(new InterruptedException(failureConnectionString)));
          resultOfAuthorization.notify();
        }
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
  public ResultHolder<User> signUp(String username, String password, String handle, String name,
      String surname) {
    final ResultHolder<User> resultOfAuthorization = new ResultHolder<>();
    Call<AuthenticationResponse> loginServiceCall = loginService.register(
        new RegisterRequest(username, handle, password, name, surname));
    loginServiceCall.enqueue(
        getCallback(resultOfAuthorization, "Username is not correct or is already taken",
            "Some connection issues happened"));
    return resultOfAuthorization;
  }
}