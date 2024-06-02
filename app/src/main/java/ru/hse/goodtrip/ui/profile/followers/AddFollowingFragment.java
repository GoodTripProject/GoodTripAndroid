package ru.hse.goodtrip.ui.profile.followers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import ru.hse.goodtrip.MainActivity;
import ru.hse.goodtrip.data.model.User;
import ru.hse.goodtrip.databinding.FragmentAddFollowingBinding;

public class AddFollowingFragment extends Fragment {

  private AddFollowingViewModel addFollowingViewModel;

  private FragmentAddFollowingBinding binding;

  @Override
  public void onResume() {
    super.onResume();
    ((MainActivity) requireActivity()).getSupportActionBar().show();
    ((MainActivity) requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    ((MainActivity) requireActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding = FragmentAddFollowingBinding.inflate(inflater, container, false);
    addFollowingViewModel =
        new ViewModelProvider(this).get(AddFollowingViewModel.class);

    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    setButtonClickListener();
  }

  private void setButtonClickListener() {
    binding.searchButton.setOnClickListener(v -> {
      String handleToFind = binding.enterHandle.getText().toString();
      addFollowingViewModel.findUser(handleToFind, (user) ->
          handleFindedUser(user, handleToFind));
    });
  }

  private void handleFindedUser(User user, String handleToFind) {
    if (user != null) {
      ((MainActivity) requireActivity()).getNavigationGraph()
          .navigateToFollowingProfilePage(user);
    } else {
      Toast.makeText(
          getContext().getApplicationContext(),
          "User was not found: ".concat(handleToFind),
          Toast.LENGTH_LONG).show();
    }
  }
}