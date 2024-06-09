package ru.hse.goodtrip.ui.profile.followers;

import static ru.hse.goodtrip.ui.profile.followers.FollowingFragment.PAGE_TYPE.FOLLOWING;
import static ru.hse.goodtrip.ui.trips.feed.utils.Utils.setImageByUrl;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import java.util.ArrayList;
import java.util.Objects;
import ru.hse.goodtrip.MainActivity;
import ru.hse.goodtrip.data.UsersRepository;
import ru.hse.goodtrip.data.model.User;
import ru.hse.goodtrip.databinding.FragmentFollowingBinding;
import ru.hse.goodtrip.databinding.ItemFollowingBinding;

/**
 * FollowingFragment that displays a Following profile page.
 */
public class FollowingFragment extends Fragment {

  private ru.hse.goodtrip.ui.profile.followers.FollowingViewModel followingViewModel;
  private FragmentFollowingBinding binding;
  private PAGE_TYPE pageType;
  private User user;

  @Override
  public void onResume() {
    super.onResume();
    Objects.requireNonNull(((MainActivity) requireActivity()).getSupportActionBar()).show();
    Objects.requireNonNull(((MainActivity) requireActivity()).getSupportActionBar())
        .setDisplayHomeAsUpEnabled(true);
    Objects.requireNonNull(((MainActivity) requireActivity()).getSupportActionBar())
        .setDisplayShowHomeEnabled(true);
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
  @SuppressWarnings("unchecked")
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    Bundle args = getArguments();
    if (args != null) {
      this.pageType = (PAGE_TYPE) args.get(ProfileFollowingFragment.PAGE_TYPE_ARG);
      this.user = (User) args.get(ProfileFollowingFragment.USER_ARG);
      followingViewModel.setUsers((ArrayList<User>) args.get(ProfileFollowingFragment.FOLLOWS_ARG));
    }

    updateFollowing();
    setButtonClickListeners();
  }

  private void setButtonClickListeners() {
    if (pageType == FOLLOWING && user == UsersRepository.getInstance().getLoggedUser()) {
      binding.addFollowingButton.setVisibility(View.VISIBLE);

      binding.addFollowingButton.setOnClickListener(
          v -> ((MainActivity) requireActivity()).getNavigationGraph().navigateToAddFollowing());
    } else {
      binding.addFollowingButton.setVisibility(View.GONE);
    }
  }

  private void updateFollowing() {
    if (pageType.equals(PAGE_TYPE.FOLLOWERS)) {
      followingViewModel.updateFollowersUsers(
          () -> new Handler(Looper.getMainLooper()).post(this::renderFollowing));
    } else {
      followingViewModel.updateFollowingUsers(
          () -> new Handler(Looper.getMainLooper()).post(this::renderFollowing));
    }
  }

  private void renderFollowing() {
    LinearLayout users = binding.following;

    for (User follow : followingViewModel.getUsers()) {
      ItemFollowingBinding followingBinding = ItemFollowingBinding.inflate(
          getLayoutInflater());
      followingBinding.displayName.setText(follow.getDisplayName());
      followingBinding.handle.setText(follow.getHandle());
      setImageByUrl(followingBinding.profileImage, follow.getMainPhotoUrl());

      followingBinding.userCard.setOnClickListener(
          v -> ((MainActivity) requireActivity()).getNavigationGraph()
              .navigateToFollowingProfilePage(follow)); // TODO: STEP TWO

      users.addView(followingBinding.getRoot());
    }
  }

  public enum PAGE_TYPE {
    FOLLOWERS, FOLLOWING
  }
}