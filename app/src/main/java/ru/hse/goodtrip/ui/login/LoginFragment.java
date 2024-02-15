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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;


import ru.hse.goodtrip.R;
import ru.hse.goodtrip.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {

    private LoginViewModel loginViewModel;
    private FragmentLoginBinding binding;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentLoginBinding.inflate(inflater, container, false);
        BottomNavigationView navView = requireActivity().findViewById(R.id.nav_view);
        navView.setVisibility(View.INVISIBLE);
        View mainFragmentsView = requireActivity().findViewById(R.id.nav_host_fragment_activity_main);
        mainFragmentsView.setVisibility(View.INVISIBLE);
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
        loginViewModel.getLoginFormState().observe(getViewLifecycleOwner(), loginFormState -> {
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

        loginViewModel.getLoginResult().observe(getViewLifecycleOwner(), loginResult -> {
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
        passwordEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
            return false;
        });

        loginButton.setOnClickListener(v -> {
            loadingProgressBar.setVisibility(View.VISIBLE);
            loginViewModel.login(usernameEditText.getText().toString(),
                    passwordEditText.getText().toString());
        });
        goToSignUpButton.setOnClickListener(v -> setRegisterFields(View.VISIBLE));
        goToLoginButton.setOnClickListener(v -> setRegisterFields(View.INVISIBLE));
        final Button signUp = binding.register;
        signUp.setOnClickListener(v -> {
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
        requireActivity().getSupportFragmentManager().popBackStack();
        BottomNavigationView navView = requireActivity().findViewById(R.id.nav_view);
        navView.setVisibility(View.VISIBLE);
        View mainFragmentsView = requireActivity().findViewById(R.id.nav_host_fragment_activity_main);
        mainFragmentsView.setVisibility(View.VISIBLE);
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