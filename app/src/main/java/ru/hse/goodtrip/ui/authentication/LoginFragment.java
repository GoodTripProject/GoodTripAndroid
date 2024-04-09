package ru.hse.goodtrip.ui.authentication;

import android.os.Bundle;
import android.os.Handler;
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
import androidx.credentials.CredentialManager;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import ru.hse.goodtrip.MainActivity;
import ru.hse.goodtrip.R;
import ru.hse.goodtrip.data.UsersRepository;
import ru.hse.goodtrip.data.model.User;
import ru.hse.goodtrip.databinding.FragmentLoginBinding;
import ru.hse.goodtrip.room.RoomImplementation;

/**
 * LoginFragment.
 */
public class LoginFragment extends Fragment {

  CredentialManager credentialManager;
  private AuthViewModel authViewModel;
  private FragmentLoginBinding binding;
  private EditText emailEditText;
  private EditText passwordEditText;
  private Button loginButton;
  private ProgressBar loadingProgressBar;
  private View signInViaGoogle;

  @Override
  public void onResume() {
    super.onResume();
    requireActivity().findViewById(R.id.bottomToolsBar).setVisibility(View.GONE);
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

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    emailEditText = binding.editTextEmail;
    passwordEditText = binding.editTextPassword;
    loginButton = binding.logInButton;
    loadingProgressBar = binding.loading;
    signInViaGoogle = binding.googleSignInButton;
    loginButton.setEnabled(false);

    authViewModel
        .getLoginFormState()
        .observe(getViewLifecycleOwner(), this::updateUiWithLoginFormState);
    authViewModel
        .getLoginResult().
        observe(getViewLifecycleOwner(), this::updateUiWithLoginResult);

    setEditTextListeners();
    setButtonListeners();
  }

  /**
   * Show errors on fields.
   *
   * @param loginFormState login form state.
   */
  private void updateUiWithLoginFormState(LoginFormState loginFormState) {
    if (loginFormState == null) {
      return;
    }
    loginButton.setEnabled(loginFormState.isDataValid());
    if (loginFormState.getUsernameError() != null) {
      emailEditText.setError(getString(loginFormState.getUsernameError()));
    } else if (loginFormState.getPasswordError() != null) {
      passwordEditText.setError(getString(loginFormState.getPasswordError()));
    }
  }

  /**
   * Navigate to main screen if login is successful.
   *
   * @param loginResult result of login request.
   */
  private void updateUiWithLoginResult(LoginResult loginResult) {
    if (loginResult == null) {
      return;
    }
    Handler handler = new Handler();
    handler.postDelayed(() -> {
      if (loginResult.getError() != null) {
        showLoginFailed(loginResult.getError());
      } else if (loginResult.getSuccess() != null) {
        loginSuccessful(loginResult.getSuccess());
      }
    }, 1000);
  }

  /**
   * Handling sign in via Google.
   *
   * @param v button clicked
   */
  private void googleSignInClickListener(View v) {
    authViewModel.sendSignInViaGoogleRequest(credentialManager, (MainActivity) requireActivity());
  }

  /**
   * Set fields change listeners.
   */
  public void setEditTextListeners() {
    final TextWatcher onDataChangedListener = new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
      }

      @Override
      public void afterTextChanged(Editable s) {
        authViewModel.loginDataChanged(emailEditText.getText().toString(),
            passwordEditText.getText().toString());
      }
    };

    emailEditText.addTextChangedListener(onDataChangedListener);
    passwordEditText.addTextChangedListener(onDataChangedListener);

    passwordEditText.setOnEditorActionListener((v, actionId, event) -> {
      if (actionId == EditorInfo.IME_ACTION_DONE) {
        loginButton.performClick();
      }
      return false;
    });
  }

  /**
   * Set buttons listeners.
   */
  public void setButtonListeners() {
    signInViaGoogle.setOnClickListener(this::googleSignInClickListener);
    loginButton.setOnClickListener(v -> {
      showLoadingView();
      login();
    });
    binding.goToSignUp.setOnClickListener(v -> navigateToSignUp());
  }

  /**
   * Navigate to main screen.
   */
  private void loginSuccessful(User user) {
    User loggedUser = UsersRepository.getInstance().getLoggedUser();
    RoomImplementation.getInstance()
        .setLoggedUser(loggedUser.getDisplayName(), passwordEditText.getText().toString());
    Log.d("LoginFragment", loggedUser.getDisplayName());
    ((MainActivity) requireActivity()).getNavigationGraph().navigateToMainGraph();
  }

  public void showLoadingView() {
    loginButton.setVisibility(View.GONE);
    loadingProgressBar.setVisibility(View.VISIBLE);
  }

  private void login() {
    authViewModel.login(emailEditText.getText().toString(),
        passwordEditText.getText().toString());
  }

  private void showLoginFailed(@StringRes Integer errorString) {
    if (getContext() != null && getContext().getApplicationContext() != null) {
      Toast.makeText(
          getContext().getApplicationContext(),
          errorString,
          Toast.LENGTH_LONG).show();
      hideLoadingView();
    }
  }

  private void navigateToSignUp() {
    ((MainActivity) requireActivity()).getNavigationGraph().navigateToSignUp();
  }


  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }
}