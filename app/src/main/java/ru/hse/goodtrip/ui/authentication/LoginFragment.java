package ru.hse.goodtrip.ui.authentication;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.credentials.Credential;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.CustomCredential;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.GetPasswordOption;
import androidx.credentials.PasswordCredential;
import androidx.credentials.exceptions.GetCredentialException;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;
import ru.hse.goodtrip.MainActivity;
import ru.hse.goodtrip.R;
import ru.hse.goodtrip.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {

  CredentialManager credentialManager;
  private AuthViewModel authViewModel;
  private FragmentLoginBinding binding;

  @Override
  public void onResume() {
    super.onResume();
    requireActivity().findViewById(R.id.bottomToolsBar).setVisibility(View.INVISIBLE);
  }

  @Override
  public void onStop() {
    super.onStop();
    requireActivity().findViewById(R.id.bottomToolsBar).setVisibility(View.VISIBLE);
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    binding = FragmentLoginBinding.inflate(inflater, container, false);
    credentialManager = CredentialManager.create(requireContext());
    authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    return binding.getRoot();
  }

  private void navigateToSignUp() {
    ((MainActivity) requireActivity()).getNavigationGraph().navigateToSignUp();
  }

  public void handleSignIn(GetCredentialResponse result) {
    Credential credential = result.getCredential();

    if (credential instanceof PasswordCredential) {
      String username = ((PasswordCredential) credential).getId();
      String password = ((PasswordCredential) credential).getPassword();
      authViewModel.login(username, password);
    } else if (credential instanceof CustomCredential) {
      if (GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL.equals(credential.getType())) {
        GoogleIdTokenCredential googleIdTokenCredential = GoogleIdTokenCredential.createFrom(
            (credential).getData());
        authViewModel.signUp(googleIdTokenCredential.getId(),
            googleIdTokenCredential.getIdToken(),
            googleIdTokenCredential.getGivenName(),
            googleIdTokenCredential.getFamilyName(),
            googleIdTokenCredential.getId());
      } else {
        binding.googleSignInButton.setVisibility(View.INVISIBLE);
      }
    } else {
      // Catch any unrecognized credential type here.
      binding.googleSignInButton.setVisibility(View.INVISIBLE);

    }
  }

  private void googleSignInClickListener(View v) {
    String serverClientId = getString(R.string.OAUTH_API_KEY);
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
        requireActivity(),
        request,
        null,
        requireActivity().getMainExecutor(),
        new CredentialManagerCallback<GetCredentialResponse, GetCredentialException>() {
          @Override
          public void onResult(GetCredentialResponse result) {
            handleSignIn(result);
          }

          @Override
          public void onError(@NonNull GetCredentialException e) {
          }
        }
    );
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    EditText usernameEditText = binding.editTextEmail;
    EditText passwordEditText = binding.editTextPassword;
    Button loginButton = binding.logInButton;
    ProgressBar loadingProgressBar = binding.loading;
    View goToSignUpButton = binding.goToSignUp;
    View signInViaGoogle = binding.googleSignInButton;

    loginButton.setEnabled(false);
    signInViaGoogle.setOnClickListener(this::googleSignInClickListener);

    authViewModel.getLoginFormState().
        observe(getViewLifecycleOwner(), loginFormState -> {
          if (loginFormState == null) {
            return;
          }
          loginButton.setEnabled(loginFormState.isDataValid());
          if (loginFormState.getUsernameError() != null) {
            usernameEditText.setError(getString(loginFormState.getUsernameError()));
          }
          if (loginFormState.getPasswordError() != null) {
            passwordEditText.setError(getString(loginFormState.getPasswordError()));
          }
        });

    authViewModel.getLoginResult().
        observe(getViewLifecycleOwner(), loginResult -> {
          if (loginResult == null) {
            return;
          }
          loadingProgressBar.setVisibility(View.GONE);
          if (loginResult.getError() != null) {
            showLoginFailed(loginResult.getError());
          }
          if (loginResult.getSuccess() != null) {
            updateUiWithUser(loginResult.getSuccess());
          }
        });

    TextWatcher afterTextChangedListener = new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // ignore
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        // ignore
      }

      @Override
      public void afterTextChanged(Editable s) {
        authViewModel.loginDataChanged(usernameEditText.getText().toString(),
            passwordEditText.getText().toString());
      }
    };
    usernameEditText.addTextChangedListener(afterTextChangedListener);
    passwordEditText.addTextChangedListener(afterTextChangedListener);
    passwordEditText.setOnEditorActionListener((v, actionId, event) -> {
      if (actionId == EditorInfo.IME_ACTION_DONE) {
        authViewModel.login(usernameEditText.getText().toString(),
            passwordEditText.getText().toString());
      }
      return false;
    });

    loginButton.setOnClickListener(v -> {
      loadingProgressBar.setVisibility(View.VISIBLE);
      authViewModel.login(usernameEditText.getText().toString(),
          passwordEditText.getText().toString());
    });

    goToSignUpButton.setOnClickListener(
        v -> ((MainActivity) requireActivity()).getNavigationGraph().navigateToSignUp());

//    goToLoginButton.setOnClickListener(v -> setRegisterFields(View.INVISIBLE));
//    final Button signUp = binding.register;
//    signUp.setOnClickListener(v -> {
//      final EditText handleEditText = binding.handle;
//      final EditText surnameEditText = binding.surname;
//      final EditText nameEditText = binding.name;
//      loadingProgressBar.setVisibility(View.VISIBLE);
//      authViewModel.signUp(usernameEditText.getText().toString(),
//          passwordEditText.getText().toString(),
//          surnameEditText.getText().toString(),
//          nameEditText.getText().toString(),
//          handleEditText.getText().toString());
//    });
  }


  private void updateUiWithUser(LoggedInUserView model) {
    ((MainActivity) requireActivity()).getNavigationGraph().navigateUp();
  }

  private void showLoginFailed(@StringRes Integer errorString) {
    if (getContext() != null && getContext().getApplicationContext() != null) {
      Toast.makeText(
          getContext().getApplicationContext(),
          errorString,
          Toast.LENGTH_LONG).show();
    }
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }
}