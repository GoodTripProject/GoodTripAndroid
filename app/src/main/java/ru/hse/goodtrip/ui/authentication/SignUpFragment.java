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
import ru.hse.goodtrip.databinding.FragmentSignUpBinding;

public class SignUpFragment extends Fragment {


  FragmentSignUpBinding binding;
  AuthViewModel authViewModel;

  CredentialManager credentialManager;
  private EditText emailEditText;
  private EditText passwordEditText;
  private EditText repeatedPasswordEditText;
  private EditText handlerEditText;
  private EditText nameEditText;
  private EditText surnameEditText;
  private ProgressBar loadingProgressBar;
  private Button signUpButton;
  private View goToLoginButton;
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


  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    binding = FragmentSignUpBinding.inflate(inflater, container, false);
    authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    credentialManager = CredentialManager.create(requireContext());
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    emailEditText = binding.editTextEmail;
    handlerEditText = binding.editTextHandler;
    surnameEditText = binding.editTextSurname;
    nameEditText = binding.editTextName;
    passwordEditText = binding.editTextPassword;
    goToLoginButton = binding.goToLogIn;
    loadingProgressBar = binding.loading;
    signUpButton = binding.signUpButton;
    repeatedPasswordEditText = binding.editTextRepeatedPassword;
    signInViaGoogle = binding.googleSignInButton;
    signUpButton.setEnabled(false);

    authViewModel.getSignUpFormState()
        .observe(getViewLifecycleOwner(), this::updateUIWithSignUpFormState);
    authViewModel.getLoginResult().
        observe(getViewLifecycleOwner(), this::updateUIWithSignUpResult);

    setButtonListeners();
    setEditTextListeners();
  }

  public void updateUIWithSignUpFormState(SignUpFormState signUpFormState) {
    if (signUpFormState == null) {
      return;
    }
    signUpButton.setEnabled(signUpFormState.isDataValid());
    if (signUpFormState.getUsernameError() != null) {
      emailEditText.setError(getString(signUpFormState.getUsernameError()));
    } else if (signUpFormState.getPasswordError() != null) {
      passwordEditText.setError(getString(signUpFormState.getPasswordError()));
    } else if (signUpFormState.getRepeatedPasswordError() != null) {
      repeatedPasswordEditText.setError(getString(signUpFormState.getRepeatedPasswordError()));
    } else if (signUpFormState.getHandlerError() != null) {
      handlerEditText.setError(getString(signUpFormState.getHandlerError()));
    }
  }

  public void updateUIWithSignUpResult(LoginResult loginResult) {
    if (loginResult == null) {
      return;
    }
    Handler handler = new Handler();
    handler.postDelayed(() -> {
      if (loginResult.getError() != null) {
        showSignUpFailed(loginResult.getError());
      } else if (loginResult.getSuccess() != null) {
        signUpSuccessful(loginResult.getSuccess());
      }
    }, 1000);
  }


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
        authViewModel.signUpDataChanged(emailEditText.getText().toString(),
            passwordEditText.getText().toString(), repeatedPasswordEditText.getText().toString(),
            handlerEditText.getText().toString());
      }
    };

    emailEditText.addTextChangedListener(onDataChangedListener);
    passwordEditText.addTextChangedListener(onDataChangedListener);
    handlerEditText.addTextChangedListener(onDataChangedListener);
    repeatedPasswordEditText.addTextChangedListener(onDataChangedListener);

    repeatedPasswordEditText.setOnEditorActionListener((v, actionId, event) -> {
      if (actionId == EditorInfo.IME_ACTION_DONE) {
        signUpButton.performClick();
      }
      return false;
    });
  }

  private void setButtonListeners() {
    goToLoginButton.setOnClickListener(this::navigateToLogin);

    signUpButton.setOnClickListener(v -> {
      showLoadingView();
      signUp();
    });

    signInViaGoogle.setOnClickListener(this::googleSignInClickListener);
  }

  private void googleSignInClickListener(View v) {
    authViewModel.sendSignInViaGoogleRequest(credentialManager, (MainActivity) requireActivity());
  }

  private void signUpSuccessful(User user) {
    User loggedUser = UsersRepository.getInstance().getLoggedUser();
    Log.d("SignUpFragment", loggedUser.getDisplayName());
    ((MainActivity) requireActivity()).getNavigationGraph().navigateUp();
    ((MainActivity) requireActivity()).getNavigationGraph().navigateUp();
  }

  private void navigateToLogin(View v) {
    ((MainActivity) requireActivity()).getNavigationGraph().navigateUp();
  }

  private void showLoadingView() {
    signUpButton.setVisibility(View.GONE);
    loadingProgressBar.setVisibility(View.VISIBLE);
  }

  private void showSignUpFailed(@StringRes Integer errorString) {
    if (getContext() != null && getContext().getApplicationContext() != null) {
      Toast.makeText(
          getContext().getApplicationContext(),
          errorString,
          Toast.LENGTH_LONG).show();
    }
  }

  private void signUp() {
    authViewModel.signUp(emailEditText.getText().toString(),
        passwordEditText.getText().toString(),
        surnameEditText.getText().toString(),
        nameEditText.getText().toString(),
        handlerEditText.getText().toString());
  }


  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }
}