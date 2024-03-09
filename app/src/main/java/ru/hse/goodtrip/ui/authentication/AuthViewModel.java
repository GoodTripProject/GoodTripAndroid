package ru.hse.goodtrip.ui.authentication;

import android.util.Patterns;
import androidx.credentials.CredentialManager;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import lombok.Getter;
import ru.hse.goodtrip.R;
import ru.hse.goodtrip.data.UsersRepository;
import ru.hse.goodtrip.data.model.Result;
import ru.hse.goodtrip.data.model.User;

/**
 * ViewModel that provides interactions with LoginService.
 */
@Getter
public class AuthViewModel extends ViewModel {

  private final MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
  private final MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
  private final UsersRepository usersRepository = UsersRepository.getInstance();

  /**
   * login user.
   *
   * @param username username
   * @param password password
   */
  public void login(String username, String password) {
    // can be launched in a separate asynchronous job
    Result<User> result = usersRepository.login(username, password);

    if (result.isSuccess()) {
      User data = ((Result.Success<User>) result).getData();
      loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
    } else {
      loginResult.setValue(new LoginResult(R.string.login_failed));
    }
  }

  /**
   * sign up user.
   *
   * @param username username.
   * @param password password.
   * @param name     name of user.
   * @param handle   handle of user.
   */
  public void signUp(String username, String password, String name, String surname,
      String handle) {
    loginDataChanged(username, password);
    Result<User> result = usersRepository.signUp(username, password, handle, surname, name);

    if (result.isSuccess()) {
      User data = ((Result.Success<User>) result).getData();
      loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
    } else {
      loginResult.setValue(new LoginResult(R.string.signup_failed));
    }
  }

  /**
   * Calls if data changed, changes the state.
   *
   * @param username username.
   * @param password password.
   */
  public void loginDataChanged(String username, String password) {
    if (!isUserNameValid(username)) {
      loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
    } else if (!isPasswordValid(password)) {
      loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
    } else {
      loginFormState.setValue(new LoginFormState(true));
    }
  }

  /**
   * A placeholder username validation check.
   *
   * @param username username.
   * @return true if name is valid, false otherwise.
   */
  private boolean isUserNameValid(String username) {
    if (username == null) {
      return false;
    }
    if (username.contains("@")) {
      return Patterns.EMAIL_ADDRESS.matcher(username).matches();
    } else {
      return !username.trim().isEmpty();
    }
  }

  /**
   * Password validation check.
   *
   * @param password password.
   * @return true if password is valid, false otherwise.
   */
  private boolean isPasswordValid(String password) {
    return password != null && password.trim().length() > 5;
  }
}