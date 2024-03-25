package ru.hse.goodtrip.data;

import java.io.IOException;
import ru.hse.goodtrip.data.model.Result;
import ru.hse.goodtrip.data.model.User;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class AuthService {

  public static final User fakeUser = new User(
      "Jane Doe", null);


  /**
   * Login user.
   *
   * @param username Username.
   * @param password Users password.
   * @return Result.
   */
  public Result login(String username, String password) {
    try {
      // TODO: handle loggedInUser authentication
      return new Result.Success(fakeUser);
    } catch (Exception e) {
      return new Result.Error(new IOException("Error logging in", e));
    }
  }

  /**
   * Sign up user.
   *
   * @param username username.
   * @param password password.
   * @param handle   users handle.
   * @param name     user name.
   * @param surname  user surname.
   * @return result value
   */
  public Result signUp(String username, String password, String handle, String name,
      String surname) {

    try {
      // TODO: handle loggedInUser authentication
      return new Result.Success(fakeUser);
    } catch (Exception e) {
      return new Result.Error(new IOException("Error logging in", e));
    }
  }

  public void logout() {
    // TODO: revoke authentication
  }
}