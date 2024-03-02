package ru.hse.goodtrip.data;

import java.io.IOException;
import ru.hse.goodtrip.data.model.LoggedInUser;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

  /**
   * Login user
   * @param username
   * @param password
   * @return
   */
  public Result<LoggedInUser> login(String username, String password) {

    try {
      // TODO: handle loggedInUser authentication
      LoggedInUser fakeUser =
          new LoggedInUser(
              java.util.UUID.randomUUID().toString(),
              "Jane Doe");
      return new Result.Success<>(fakeUser);
    } catch (Exception e) {
      return new Result.Error(new IOException("Error logging in", e));
    }
  }

  /**
   * Sign up user
   * @param username
   * @param password
   * @param handle
   * @param name
   * @param surname
   * @return
   */
  public Result<LoggedInUser> signup(String username, String password, String handle, String name,
      String surname) {

    try {
      // TODO: handle loggedInUser authentication
      LoggedInUser fakeUser =
          new LoggedInUser(
              java.util.UUID.randomUUID().toString(),
              "Jane Doe");
      return new Result.Success<>(fakeUser);
    } catch (Exception e) {
      return new Result.Error(new IOException("Error logging in", e));
    }
  }

  public void logout() {
    // TODO: revoke authentication
  }
}