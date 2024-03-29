package ru.hse.goodtrip.ui.authentication;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.Patterns;
import androidx.annotation.NonNull;
import androidx.credentials.Credential;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.CustomCredential;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.GetPasswordOption;
import androidx.credentials.PasswordCredential;
import androidx.credentials.exceptions.GetCredentialException;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.Getter;
import ru.hse.goodtrip.MainActivity;
import ru.hse.goodtrip.R;
import ru.hse.goodtrip.data.UsersRepository;
import ru.hse.goodtrip.data.model.Result;
import ru.hse.goodtrip.data.model.ResultHolder;
import ru.hse.goodtrip.data.model.User;

/**
 * ViewModel that provides interactions with LoginService.
 */
@Getter
public class AuthViewModel extends ViewModel {

  private final static String TAG = "AuthViewModel";
  private final MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
  private final MutableLiveData<SignUpFormState> signUpFormState = new MutableLiveData<>();
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
    ResultHolder<User> result = usersRepository.login(username, password);
    runExecutorToWaitResult(result);


  }

  private void runExecutorToWaitResult(ResultHolder<User> result) {
    ExecutorService executor = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());
    executor.execute(() -> {
      synchronized (result) {
        try {
          result.wait();
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
      }
      handler.post(() -> {
        if (result.getResult().isSuccess()) {
          User data = ((Result.Success<User>) result.getResult()).getData();
          loginResult.setValue(new LoginResult(data));
        } else {
          loginResult.setValue(new LoginResult(R.string.login_failed));
        }
      });
    });
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
    ResultHolder<User> result = usersRepository.signUp(username, password, handle, surname, name);
    runExecutorToWaitResult(result);
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
   * Calls if data changed, changes the state.
   *
   * @param username         username.
   * @param password         password.
   * @param handler          handler.
   * @param repeatedPassword repeated password.
   */
  public void signUpDataChanged(String username, String password, String repeatedPassword,
      String handler) {
    if (!isUserNameValid(username)) {
      signUpFormState.setValue(new SignUpFormState(R.string.invalid_username, null, null, null));
    } else if (!isPasswordValid(password)) {
      signUpFormState.setValue(new SignUpFormState(null, R.string.invalid_password, null, null));
    } else if (!isRepeatedPasswordMatches(password, repeatedPassword)) {
      signUpFormState.setValue(
          new SignUpFormState(null, null, null, R.string.invalid_repeat_password));
    } else if (!isHandlerValid(handler)) {
      signUpFormState.setValue(new SignUpFormState(null, null, R.string.invalid_handler, null));
    } else {
      signUpFormState.setValue(new SignUpFormState(true));
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

  /**
   * Password and repeated password matching check.
   *
   * @param password         password.
   * @param repeatedPassword repeatedPassword.
   * @return true if passwords are matching.
   */
  private boolean isRepeatedPasswordMatches(String password, String repeatedPassword) {
    return password.equals(repeatedPassword);
  }

  /**
   * Handler validation.
   *
   * @param handler user handler.
   * @return true if handler is valid.
   */
  private boolean isHandlerValid(String handler) {
    //TODO
    return true;
  }

  /**
   * Send request to sign in via Google and handle response if successful.
   *
   * @param credentialManager credential manager.
   * @param context           context.
   */
  public void sendSignInViaGoogleRequest(CredentialManager credentialManager,
      MainActivity context) {
    String serverClientId = context.getResources().getString(R.string.OAUTH_API_KEY);

    GetGoogleIdOption googleIdOption = new GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false)
        .setServerClientId(serverClientId)
        .build();
    GetPasswordOption getPasswordOption = new GetPasswordOption();

    GetCredentialRequest request = new GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .addCredentialOption(getPasswordOption)
        .build();

    credentialManager.getCredentialAsync(
        context,
        request,
        null,
        context.getMainExecutor(),
        new CredentialManagerCallback<GetCredentialResponse, GetCredentialException>() {
          @Override
          public void onResult(GetCredentialResponse result) {
            handleGoogleSignIn(result);
          }

          @Override
          public void onError(@NonNull GetCredentialException e) {
          }
        }
    );
  }


  /**
   * Handle sign in via Google.
   *
   * @param result result of sign in response.
   */
  public void handleGoogleSignIn(GetCredentialResponse result) {
    Credential credential = result.getCredential();

    if (credential instanceof PasswordCredential) {
      String username = ((PasswordCredential) credential).getId();
      String password = ((PasswordCredential) credential).getPassword();
      login(username, password);
    } else if (credential instanceof CustomCredential) {
      if (GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL.equals(credential.getType())) {
        GoogleIdTokenCredential googleIdTokenCredential = GoogleIdTokenCredential.createFrom(
            (credential).getData());
        signUp(googleIdTokenCredential.getId(),
            googleIdTokenCredential.getIdToken(),
            googleIdTokenCredential.getGivenName(),
            googleIdTokenCredential.getFamilyName(),
            googleIdTokenCredential.getId());
      } else {
        Log.e(TAG, "Unexpected type of credential");
      }
    } else {
      Log.e(TAG, "Unexpected type of credential");
    }
  }

}