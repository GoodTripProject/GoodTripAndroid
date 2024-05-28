package ru.hse.goodtrip.ui.profile.following;

import static ru.hse.goodtrip.ui.trips.feed.utils.Utils.setImageByUrl;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import ru.hse.goodtrip.MainActivity;
import ru.hse.goodtrip.data.model.User;
import ru.hse.goodtrip.databinding.FragmentProfileFollowingBinding;

public class ProfileFollowingFragment extends Fragment {

  public final static String USER_ARG = "user";
  private ProfileFollowingViewModel profileFollowingViewModel;
  private FragmentProfileFollowingBinding binding;
  private User user;

  @Override
  public void onResume() {
    super.onResume();
    ((MainActivity) requireActivity()).getSupportActionBar().show();
    ((MainActivity) requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    ((MainActivity) requireActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater,
      ViewGroup container, Bundle savedInstanceState) {
    profileFollowingViewModel =
        new ViewModelProvider(this).get(ProfileFollowingViewModel.class);
    binding = FragmentProfileFollowingBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    Bundle args = getArguments();
    if (args != null) {
      this.user = (User) args.get(ProfileFollowingFragment.USER_ARG);
      profileFollowingViewModel.setUser(user);
    }
    setUserInfo();
    setButtonClickListeners();
  }

  private void setButtonClickListeners() {
    final Button showMapButton = binding.showMapButton;
    showMapButton.setOnClickListener(
        v -> ((MainActivity) requireActivity()).getNavigationGraph().navigateToFollowingMap(user));
  }

  private void setUserInfo() {
    if (user.getMainPhotoUrl() != null) {
      setImageByUrl(binding.profileImage, user.getMainPhotoUrl().toString());
    } else {
      setImageByUrl(binding.profileImage, // TODO:
          "https://hosting.photobucket.com/albums/ii87/aikhrabrov/Paris%20la%20nuit/img_6910.jpg");
    }
    binding.displayName.setText(user.getDisplayName());
    binding.handler.setText("@".concat(user.getHandle()));
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }
}