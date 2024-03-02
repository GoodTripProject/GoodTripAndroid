package ru.hse.goodtrip.ui.login;

import android.util.Patterns;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import ru.hse.goodtrip.R;
import ru.hse.goodtrip.data.LoginRepository;
import ru.hse.goodtrip.data.Result;
import ru.hse.goodtrip.data.model.LoggedInUser;

public class LoginViewModel extends ViewModel {

  private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
  private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
  private LoginRepository loginRepository;

  LoginViewModel(LoginRepository loginRepository) {
    this.loginRepository = loginRepository;
  }

  LiveData<LoginFormState> getLoginFormState() {
    return loginFormState;
  }

  LiveData<LoginResult> getLoginResult() {
    return loginResult;
  }

  /**
   * login user.
   *
   * @param username username
   * @param password password
   */
  public void login(String username, String password) {
    // can be launched in a separate asynchronous job
    Result<LoggedInUser> result = loginRepository.login(username, password);

    if (result instanceof Result.Success) {
      LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
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
   * @param surname surname of user.
   * @param name name of user.
   * @param handle handle of user.
   */
  public void signUp(String username, String password,
      String surname, String name,
      String handle) {
    loginDataChanged(username, password);
    Result<LoggedInUser> result = loginRepository.signUp(username, password, handle, name, surname);

    if (result instanceof Result.Success) {
      LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
      loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
    } else {
      loginResult.setValue(new LoginResult(R.string.signup_failed));
    }
  }

  /**
   * calls if data changed, changes the state.
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
   * checks user name.
   *
   * @param username username.
   * @return true if name is valid, false otherwise.
   */
  // A placeholder username validation check
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