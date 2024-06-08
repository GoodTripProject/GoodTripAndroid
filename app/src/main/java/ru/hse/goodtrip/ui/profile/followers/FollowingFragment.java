package ru.hse.goodtrip.ui.profile.followers;

import static ru.hse.goodtrip.ui.profile.followers.FollowingFragment.PAGE_TYPE.FOLLOWING;
import static ru.hse.goodtrip.ui.trips.feed.utils.Utils.setImageByUrl;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import java.util.List;
import ru.hse.goodtrip.MainActivity;
import ru.hse.goodtrip.data.model.User;
import ru.hse.goodtrip.databinding.FragmentFollowingBinding;
import ru.hse.goodtrip.databinding.ItemFollowingBinding;

/**
 * FollowingFragment that displays a Following profile page.
 */
public class FollowingFragment extends Fragment {

  private FollowingViewModel followingViewModel;
  private FragmentFollowingBinding binding;
  private PAGE_TYPE pageType;

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
    followingViewModel =
        new ViewModelProvider(this).get(FollowingViewModel.class);

    binding = FragmentFollowingBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    Bundle args = getArguments();
    if (args != null) {
      this.pageType = (PAGE_TYPE) args.get(ProfileFollowingFragment.PAGE_TYPE_ARG);
      if (pageType != null) {
        Log.d("ASD", pageType.toString());
      }
    }

    renderFollowing();
    setButtonClickListeners();
  }

  private void setButtonClickListeners() {
    if (pageType == FOLLOWING) {
      binding.addFollowingButton.setVisibility(View.VISIBLE);

      binding.addFollowingButton.setOnClickListener(
          v -> ((MainActivity) requireActivity()).getNavigationGraph().navigateToAddFollowing());
    } else {
      binding.addFollowingButton.setVisibility(View.GONE);
    }
  }

  private void renderFollowing() {
    LinearLayout users = binding.following;

    List<User> usersToShow;
    if (pageType == PAGE_TYPE.FOLLOWERS) {
      usersToShow = followingViewModel.getFollowers();
    } else {
      usersToShow = followingViewModel.getFollowing();
    }

    for (User user : usersToShow) {
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

  public enum PAGE_TYPE {
    FOLLOWERS, FOLLOWING
  }
}