package ru.hse.goodtrip.ui.profile.followers;

import static ru.hse.goodtrip.ui.trips.feed.utils.Utils.setImageByUrl;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import ru.hse.goodtrip.MainActivity;
import ru.hse.goodtrip.data.UsersRepository;
import ru.hse.goodtrip.data.model.User;
import ru.hse.goodtrip.databinding.FragmentProfileFollowingBinding;
import ru.hse.goodtrip.ui.profile.followers.FollowingFragment.PAGE_TYPE;

/**
 * ProfileFollowingFragment.
 */
public class ProfileFollowingFragment extends Fragment {

  public final static String USER_ARG = "user";
  public final static String FOLLOWS_ARG = "follows";

  public final static String PAGE_TYPE_ARG = "page_type";

  private ProfileFollowingViewModel profileFollowingViewModel;
  private FragmentProfileFollowingBinding binding;
  private User user;

  private boolean isFollowing;

  @Override
  public void onResume() {
    super.onResume();
    ((MainActivity) requireActivity()).showActionBar();
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
    isFollowing = UsersRepository.getInstance().getFollowing().contains(user);
    Handler handler = Handler.createAsync(Looper.getMainLooper());
    profileFollowingViewModel.refreshFollow(() -> handler.post(this::setUserInfo));

    setupFollowButton();
    setUserInfo();
    setButtonClickListeners();
  }

  private void setupFollowButton() {
    TextView followButton = binding.follow;
    TextView followedButton = binding.followed;

    followButton.setOnClickListener(this::followUser);
    followedButton.setOnClickListener(this::unfollowUser);

    switchFollow(this.isFollowing);
  }

  private void switchFollow(boolean isFollowing) {
    this.isFollowing = isFollowing;

    TextView followButton = binding.follow;
    TextView followedButton = binding.followed;

    if (isFollowing) {
      followButton.setVisibility(View.GONE);
      followedButton.setVisibility(View.VISIBLE);
    } else {
      followButton.setVisibility(View.VISIBLE);
      followedButton.setVisibility(View.GONE);
    }
  }

  private void followUser(View v) {
    switchFollow(true);
    profileFollowingViewModel.follow(user);
  }

  private void unfollowUser(View v) {
    switchFollow(false);
    profileFollowingViewModel.unfollow(user);
  }

  private void setButtonClickListeners() {
    final Button showMapButton = binding.showMapButton;
    final LinearLayout showFollowers = binding.showFollowers;
    final LinearLayout showFollowing = binding.showFollowing;

    showMapButton.setOnClickListener(
        v -> ((MainActivity) requireActivity()).getNavigationGraph()
            .navigateToFollowingMap(user));

    showFollowers.setOnClickListener(
        v -> ((MainActivity) requireActivity()).getNavigationGraph()
            .navigateToFollowing(user, profileFollowingViewModel.getFollowers(),
                PAGE_TYPE.FOLLOWERS));

    showFollowing.setOnClickListener(
        v -> ((MainActivity) requireActivity()).getNavigationGraph()
            .navigateToFollowing(user, profileFollowingViewModel.getFollowing(),
                PAGE_TYPE.FOLLOWING));
  }

  @SuppressLint("SetTextI18n")
  private void setUserInfo() {
    if (user.getMainPhotoUrl() != null) {
      setImageByUrl(binding.profileImage,
          (user.getMainPhotoUrl() != null) ? user.getMainPhotoUrl().toString() : null);
    }
    binding.displayName.setText(user.getDisplayName());
    binding.handler.setText("@".concat(user.getHandle()));

    binding.followersCount.setText(
        Integer.toString(profileFollowingViewModel.getFollowers().size()));
    binding.followingCount.setText(
        Integer.toString(profileFollowingViewModel.getFollowing().size()));
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }
}