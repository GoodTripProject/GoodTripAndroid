package ru.hse.goodtrip.data;

import ru.hse.goodtrip.data.model.Result;
import ru.hse.goodtrip.data.model.User;

/**
 * Class that requests authentication and user information from the remote data source and maintains
 * an in-memory cache of login status and user credentials information.
 */
public class UsersRepository {

  private static volatile UsersRepository instance;

  private final LoginService dataSource;

  private User user = null;

  private UsersRepository() {
    this.dataSource = new LoginService();
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
    user = null;
    dataSource.logout();
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
  public Result<User> login(String username, String password) {
    Result<User> result = dataSource.login(username, password);
    if (result.isSuccess()) {
      setLoggedInUser(((Result.Success<User>) result).getData());
    }
    return result;
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
  public Result<User> signUp(String username, String password, String handle, String name,
      String surname) {
    Result<User> result = dataSource.signUp(username, password, handle, name, surname);
    if (result.isSuccess()) {
      setLoggedInUser(((Result.Success<User>) result).getData());
    }
    return result;
  }

  public User checkIfAlreadyLoggedIn() {
//    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(get);
//    boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
//    User.getInstance().setLoggedIn(isLoggedIn);
    return null;
  }
}