package ru.hse.goodtrip.data;

import java.io.IOException;
import lombok.Setter;
import retrofit2.Call;
import retrofit2.Response;
import ru.hse.goodtrip.data.model.Result;
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

  private static volatile UsersRepository instance;

  private final LoginService loginService;

  private User user = null;
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
  public Result login(String username, String password) {
    Call<AuthenticationResponse> loginServiceCall = loginService.login(
        new AuthorizationRequest(username, password));
    try {
      loginServiceCall.wait();
      Response<AuthenticationResponse> response = loginServiceCall.execute();
      if (!response.isSuccessful()) {
        return new Result.Error(new InterruptedException("Call of login failed"));
      }
      AuthenticationResponse authenticationResponse = response.body();
      if (authenticationResponse == null) {
        return new Result.Error(new InterruptedException("Call of login failed"));
      }
      setLoggedInUser(
          new User(authenticationResponse.getName() + " " + authenticationResponse.getSurname(),
              authenticationResponse.getUrl()));
    } catch (InterruptedException | IOException e) {
      return new Result.Error(e);
    }
    return new Result.Success(user);
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
  public Result signUp(String username, String password, String handle, String name,
      String surname) {
    Call<AuthenticationResponse> registerServiceCall = loginService.register(
        new RegisterRequest(username, handle, password, name, surname));
    try {
      registerServiceCall.wait();
      Response<AuthenticationResponse> response = registerServiceCall.execute();
      if (!response.isSuccessful()) {
        return new Result.Error(new InterruptedException("Call of login failed"));
      }
      AuthenticationResponse authenticationResponse = response.body();
      if (authenticationResponse == null) {
        return new Result.Error(new InterruptedException("Call of login failed"));
      }
      userToken = authenticationResponse.getToken();
      setLoggedInUser(
          new User(authenticationResponse.getName() + " " + authenticationResponse.getSurname(),
              authenticationResponse.getUrl()));
    } catch (InterruptedException | IOException e) {
      return new Result.Error(e);
    }
    return new Result.Success(user);
  }
}