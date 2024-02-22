package ru.hse.goodtrip.ui.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import androidx.navigation.Navigation;
import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;
import ru.hse.goodtrip.R;
import ru.hse.goodtrip.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {

  CredentialManager credentialManager;
  private LoginViewModel loginViewModel;
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

    return binding.getRoot();
  }

  private void setRegisterFields(int visibilityOption) {
    final Button registerButton = binding.register;
    final EditText handleEditText = binding.handle;
    final EditText surnameEditText = binding.surname;
    final EditText nameEditText = binding.name;
    final Button goToLoginButton = binding.goToLogin;
    goToLoginButton.setVisibility(visibilityOption);
    nameEditText.setVisibility(visibilityOption);
    surnameEditText.setVisibility(visibilityOption);
    handleEditText.setVisibility(visibilityOption);
    registerButton.setVisibility(visibilityOption);
    final Button goToSignUpButton = binding.goToSignUp;
    final Button login = binding.login;
    final Button signUp = binding.register;
    if (visibilityOption == View.INVISIBLE) {
      goToLoginButton.setVisibility(View.INVISIBLE);
      goToSignUpButton.setVisibility(View.VISIBLE);
      login.setVisibility(View.VISIBLE);
      signUp.setVisibility(View.INVISIBLE);
    } else {
      goToLoginButton.setVisibility(View.VISIBLE);
      goToSignUpButton.setVisibility(View.INVISIBLE);
      login.setVisibility(View.INVISIBLE);
      signUp.setVisibility(View.VISIBLE);
    }
  }

  public void handleSignIn(GetCredentialResponse result) {
    Credential credential = result.getCredential();

    if (credential instanceof PasswordCredential) {
      String username = ((PasswordCredential) credential).getId();
      String password = ((PasswordCredential) credential).getPassword();
      loginViewModel.login(username, password);
    } else if (credential instanceof CustomCredential) {
      if (GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL.equals(credential.getType())) {
        GoogleIdTokenCredential googleIdTokenCredential = GoogleIdTokenCredential.createFrom(
            (credential).getData());
        loginViewModel.signUp(googleIdTokenCredential.getId(),
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

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
        .get(LoginViewModel.class);
    final EditText usernameEditText = binding.username;
    final EditText passwordEditText = binding.password;
    final Button loginButton = binding.login;
    final ProgressBar loadingProgressBar = binding.loading;
    final Button goToSignUpButton = binding.goToSignUp;
    final Button goToLoginButton = binding.goToLogin;
    setRegisterFields(View.INVISIBLE);
    goToSignUpButton.setEnabled(true);
    goToLoginButton.setEnabled(true);
    final Button signInViaGoogle = binding.googleSignInButton;
    signInViaGoogle.setEnabled(true);
    signInViaGoogle.setOnClickListener(v -> {
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

    });

    loginViewModel.getLoginFormState().

        observe(getViewLifecycleOwner(), loginFormState ->

        {
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

    loginViewModel.getLoginResult().

        observe(getViewLifecycleOwner(), loginResult ->

        {
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
        loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
            passwordEditText.getText().toString());
      }
    };
    usernameEditText.addTextChangedListener(afterTextChangedListener);
    passwordEditText.addTextChangedListener(afterTextChangedListener);
    passwordEditText.setOnEditorActionListener((v, actionId, event) ->

    {
      if (actionId == EditorInfo.IME_ACTION_DONE) {
        loginViewModel.login(usernameEditText.getText().toString(),
            passwordEditText.getText().toString());
      }
      return false;
    });

    loginButton.setOnClickListener(v ->

    {
      loadingProgressBar.setVisibility(View.VISIBLE);
      loginViewModel.login(usernameEditText.getText().toString(),
          passwordEditText.getText().toString());
    });
    goToSignUpButton.setOnClickListener(v ->

        setRegisterFields(View.VISIBLE));
    goToLoginButton.setOnClickListener(v ->

        setRegisterFields(View.INVISIBLE));
    final Button signUp = binding.register;
    signUp.setOnClickListener(v ->

    {
      final EditText handleEditText = binding.handle;
      final EditText surnameEditText = binding.surname;
      final EditText nameEditText = binding.name;
      loadingProgressBar.setVisibility(View.VISIBLE);
      loginViewModel.signUp(usernameEditText.getText().toString(),
          passwordEditText.getText().toString(),
          surnameEditText.getText().toString(),
          nameEditText.getText().toString(),
          handleEditText.getText().toString());
    });
  }


  private void updateUiWithUser(LoggedInUserView model) {
    Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_container)
        .popBackStack();
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