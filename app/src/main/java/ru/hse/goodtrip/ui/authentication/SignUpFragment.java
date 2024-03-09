package ru.hse.goodtrip.ui.authentication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.credentials.CredentialManager;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import ru.hse.goodtrip.MainActivity;
import ru.hse.goodtrip.R;
import ru.hse.goodtrip.databinding.FragmentSignUpBinding;

public class SignUpFragment extends Fragment {

  CredentialManager credentialManager;

  FragmentSignUpBinding binding;
  AuthViewModel authViewModel;

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

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    binding = FragmentSignUpBinding.inflate(inflater, container, false);
    authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    credentialManager = CredentialManager.create(requireContext());
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    View goToLogInButton = binding.goToLogIn;

    goToLogInButton.setOnClickListener(
        v -> ((MainActivity) requireActivity()).getNavigationGraph().navigateUp());
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }
}