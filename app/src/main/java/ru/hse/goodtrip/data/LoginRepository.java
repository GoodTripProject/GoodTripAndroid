package ru.hse.goodtrip.data;

import ru.hse.goodtrip.data.model.LoggedInUser;

/**
 * Class that requests authentication and user information from the remote data source and maintains
 * an in-memory cache of login status and user credentials information.
 */
public class LoginRepository {

  private static volatile LoginRepository instance;

  private LoginDataSource dataSource;

  // If user credentials will be cached in local storage, it is recommended it be encrypted
  // @see https://developer.android.com/training/articles/keystore
  private LoggedInUser user = null;

  // private constructor : singleton access
  private LoginRepository(LoginDataSource dataSource) {
    this.dataSource = dataSource;
  }

  /**
   * Get instance of Login Repository.
   *
   * @param dataSource login data source.
   * @return login repository.
   */
  public static LoginRepository getInstance(LoginDataSource dataSource) {
    if (instance == null) {
      instance = new LoginRepository(dataSource);
    }
    return instance;
  }

  public boolean isLoggedIn() {
    return user != null;
  }

  public void logout() {
    user = null;
    dataSource.logout();
  }

  /**
   * set logged in user.
   *
   * @param user LoggedInUser user.
   */
  private void setLoggedInUser(LoggedInUser user) {
    this.user = user;
    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
  }

  /**
   * login user.
   *
   * @param username user name.
   * @param password password.
   * @return result value.
   */
  public Result<LoggedInUser> login(String username, String password) {
    // handle login
    Result<LoggedInUser> result = dataSource.login(username, password);
    if (result instanceof Result.Success) {
      setLoggedInUser(((Result.Success<LoggedInUser>) result).getData());
    }
    return result;
  }

  /**
   * Sign up user.
   *
   * @param username username.
   * @param password password.
   * @param handle handle.
   * @param name name of user.
   * @param surname surname of user.
   * @return result value.
   */
  public Result<LoggedInUser> signUp(String username, String password, String handle, String name,
      String surname) {
    // handle signup
    Result<LoggedInUser> result = dataSource.signup(username, password, handle, name, surname);
    if (result instanceof Result.Success) {
      setLoggedInUser(((Result.Success<LoggedInUser>) result).getData());
    }
    return result;
  }
}