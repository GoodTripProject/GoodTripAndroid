package ru.hse.goodtrip.ui.profile.following;

import static ru.hse.goodtrip.ui.trips.feed.utils.Utils.setImageByUrl;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import ru.hse.goodtrip.MainActivity;
import ru.hse.goodtrip.data.model.User;
import ru.hse.goodtrip.databinding.FragmentFollowingBinding;
import ru.hse.goodtrip.databinding.ItemFollowingBinding;

public class FollowingFragment extends Fragment {

  private FollowingViewModel followingViewModel;
  private FragmentFollowingBinding binding;

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    followingViewModel =
        new ViewModelProvider(this).get(FollowingViewModel.class);

    binding = FragmentFollowingBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    renderFollowing();
  }

  private void renderFollowing() {
    LinearLayout users = binding.following;
    for (User user : followingViewModel.getUsers()) {
      ItemFollowingBinding followingBinding = ItemFollowingBinding.inflate(
          getLayoutInflater());
      followingBinding.displayName.setText(user.getDisplayName());
      followingBinding.handle.setText(user.getHandle());
      setImageByUrl(followingBinding.profileImage, user.getMainPhotoUrl());

      followingBinding.userCard.setOnClickListener(
          v -> ((MainActivity) requireActivity()).getNavigationGraph()
              .navigateToFollowingProfilePage(user));

      users.addView(followingBinding.getRoot());
    }
  }
}