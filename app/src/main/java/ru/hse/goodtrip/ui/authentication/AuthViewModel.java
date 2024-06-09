package ru.hse.goodtrip.ui.authentication;

import static com.google.common.hash.Hashing.sha256;

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
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import lombok.Getter;
import ru.hse.goodtrip.MainActivity;
import ru.hse.goodtrip.R;
import ru.hse.goodtrip.data.TripRepository;
import ru.hse.goodtrip.data.UsersRepository;
import ru.hse.goodtrip.data.model.Result;
import ru.hse.goodtrip.data.model.User;
import ru.hse.goodtrip.network.authentication.model.AuthenticationResponse;

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

    String hashedPassword = String.valueOf(sha256().hashString(password, StandardCharsets.UTF_8));
    CompletableFuture<Result<AuthenticationResponse>> result = usersRepository.login(username,
        hashedPassword);
    runExecutorToWaitResult(result,
        () -> loginResult.setValue(new LoginResult(R.string.login_failed)));
  }

  private void runExecutorToWaitResult(CompletableFuture<Result<AuthenticationResponse>> future,
      Runnable troublesHandler) {
    Handler handler = new Handler(Looper.getMainLooper());
    future.whenCompleteAsync((result, throwable) -> handler.post(() -> {
      if (result.isSuccess()) {
        AuthenticationResponse response =
            ((Result.Success<AuthenticationResponse>) result).getData();
        User data = new User(response.getId(), response.getHandle(),
            response.getName() + " " + response.getSurname(),
            response.getUrl(), response.getToken());
        TripRepository.getInstance()
            .getUserTrips(usersRepository.user.getId(), usersRepository.user.getToken());
        loginResult.setValue(new LoginResult(data));
      } else {
        troublesHandler.run();
      }
    }));
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
    String hashedPassword = String.valueOf(sha256().hashString(password, StandardCharsets.UTF_8));

    CompletableFuture<Result<AuthenticationResponse>> result = usersRepository.signUp(username,
        hashedPassword, handle,
        surname, name);
    runExecutorToWaitResult(result,
        () -> loginResult.setValue(new LoginResult(R.string.login_failed)));
  }

  /**
   * Calls if data changed, changes the state.
   *
   * @param username username.
   * @param password password.
   */
  public void loginDataChanged(String username, String password) {
    if (isUserNameNotValid(username)) {
      loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
    } else if (isPasswordNotValid(password)) {
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
    if (isUserNameNotValid(username)) {
      signUpFormState.setValue(new SignUpFormState(R.string.invalid_username, null, null, null));
    } else if (isPasswordNotValid(password)) {
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
  private boolean isUserNameNotValid(String username) {
    if (username == null) {
      return true;
    }
    if (username.contains("@")) {
      return !Patterns.EMAIL_ADDRESS.matcher(username).matches();
    } else {
      return username.trim().isEmpty();
    }
  }

  /**
   * Password validation check.
   *
   * @param password password.
   * @return true if password is valid, false otherwise.
   */
  private boolean isPasswordNotValid(String password) {
    return password == null || password.trim().length() <= 5;
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
    return handler.length() <= 64;
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
    Log.d(TAG, "Google id Option: " + googleIdOption);
    GetPasswordOption getPasswordOption = new GetPasswordOption();
    Log.d(TAG, "Google password Option: " + getPasswordOption);

    GetCredentialRequest request = new GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .addCredentialOption(getPasswordOption)
        .build();
    Log.d(TAG, "Google GetCredentialRequest: " + request);
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
            loginResult.setValue(new LoginResult(R.string.google_authorization_failed));
          }
        }
    );
  }

  /**
   * Firstly, send request to sign up new user with data from Google account, if this user exists,
   * function send request to login him and updates data.
   *
   * @param username username.
   * @param password password.
   * @param name     name.
   * @param surname  surname.
   * @param handle   handle.
   */
  private void googleSignUp(String username, String password, String name, String surname,
      String handle) {
    loginDataChanged(username, password);
    CompletableFuture<Result<AuthenticationResponse>> result = usersRepository.login(username,
        password); //try to login
    runExecutorToWaitResult(result, () -> {
      // if login failed trying to sign up user
      signUp(username, password, name, surname, handle);
    });
  }

  /**
   * Handle sign in via Google.
   *
   * @param result result of sign in response.
   */
  public void handleGoogleSignIn(GetCredentialResponse result) {
    Credential credential = result.getCredential();
    Log.d(TAG, "Trying to auth with Google, credential: " + credential);
    if (credential instanceof PasswordCredential) {
      Log.d(TAG, "Trying to auth with Google, credential is PasswordCredential: " + credential);
      String username = ((PasswordCredential) credential).getId();
      String password = ((PasswordCredential) credential).getPassword();
      login(username, password);
    } else if (credential instanceof CustomCredential) {
      Log.d(TAG, "Trying to auth with Google, credential is : " + credential.getType() + " data is "
          + credential.getData().getString("password"));
      if (GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL.equals(credential.getType())) {
        GoogleIdTokenCredential googleIdTokenCredential = GoogleIdTokenCredential.createFrom(
            (credential).getData());
        Log.d(TAG, "GOOGLE id: " + googleIdTokenCredential.getId() + "\nid token:"
            + googleIdTokenCredential.getIdToken());
        googleSignUp(googleIdTokenCredential.getId(),
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