package ru.hse.goodtrip.navigation;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import java.util.ArrayList;
import java.util.Objects;
import ru.hse.goodtrip.MainActivity;
import ru.hse.goodtrip.R;
import ru.hse.goodtrip.data.model.User;
import ru.hse.goodtrip.data.model.trips.Trip;
import ru.hse.goodtrip.databinding.ActivityMainBinding;
import ru.hse.goodtrip.ui.profile.followers.FollowingFragment.PAGE_TYPE;
import ru.hse.goodtrip.ui.profile.followers.ProfileFollowingFragment;
import ru.hse.goodtrip.ui.trips.feed.FeedAdapter;

/**
 * Main application navigation fragment.
 */
public class GtNavigationGraphMain extends NavHostFragment {

  private final MainActivity activity;
  private final ActivityMainBinding binding;
  private NavController navController;

  /**
   * @param activity Activity context navigation associated with.
   * @param binding  Binding context navigation appeared in.
   */
  public GtNavigationGraphMain(MainActivity activity, ActivityMainBinding binding) {
    this.activity = activity;
    this.binding = binding;
    initializeNavigation();
  }

  /**
   * Initialize main navigation graph.
   */
  public void initializeNavigation() {
    Toolbar actionBar = activity.findViewById(R.id.my_toolbar);
    activity.setSupportActionBar(actionBar);
    Objects.requireNonNull(activity.getSupportActionBar()).hide();

    // Set up top level destinations.
    AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
        R.id.feed_navigation_graph, R.id.navigation_map, R.id.navigation_places,
        R.id.profile_navigation_graph)
        .build();

    navController = Navigation.findNavController(activity, R.id.nav_host_fragment_container);
    NavigationUI.setupWithNavController(binding.bottomNavigationView, navController);
    NavigationUI.setupActionBarWithNavController(activity, navController, appBarConfiguration);
    setupButtonsClickListeners();
  }

  public void setupButtonsClickListeners() {
    binding.planTripButton.setOnClickListener(v -> navigateToPlanTrip());
  }

  public void navigateToLogin() {
    navController.navigate(R.id.auth_navigation_graph);
  }

  public void navigateToSignUp() {
    navController.navigate(R.id.navigation_sign_up);
  }

  /**
   * Navigates to main navigation graph.
   */
  public void navigateToMainGraph() {
    Objects.requireNonNull(activity.getSupportActionBar()).hide();
    navController.navigate(R.id.main_navigation_graph);
  }

  /**
   * Opens ProfileFollowingFragment with provided user.
   *
   * @param following user to show.
   */
  public void navigateToFollowingMap(User following) {
    Bundle bundle = new Bundle();
    bundle.putSerializable(ProfileFollowingFragment.USER_ARG, following);
    navController.navigate(R.id.navigation_map_following, bundle);
  }

  /**
   * Navigate to MainNavigationGraph and then navigate to PostPage.
   *
   * @param trip trip to appear.
   */
  public void navigateToPostPageExternal(Trip trip) {
    activity.getNavigationGraph().navigateToMainGraph();
    activity.getNavigationGraph().navigateToPostPage(trip);
  }

  public void navigateToAddFollowing() {
    navController.navigate(R.id.navigation_add_following);
  }

  /**
   * Opens MyTripsFragment.
   */
  public void navigateToMyTrips() {
    navController.navigate(R.id.navigation_my_trips);
  }

  /**
   * Opens FollowingFragment with provided user and lists of users to appear.
   */
  public void navigateToFollowing(User user, ArrayList<User> follows, PAGE_TYPE pageType) {
    Bundle bundle = new Bundle();
    bundle.putSerializable(ProfileFollowingFragment.USER_ARG, user);
    bundle.putSerializable(ProfileFollowingFragment.FOLLOWS_ARG, follows);
    bundle.putSerializable(ProfileFollowingFragment.PAGE_TYPE_ARG, pageType);

    navController.navigate(R.id.navigation_following, bundle);
  }

  /**
   * Opens ProfileFollowingFragment with provided user.
   *
   * @param following user to show.
   */
  public void navigateToFollowingProfilePage(User following) {
    Objects.requireNonNull(activity.getSupportActionBar()).hide();

    Bundle bundle = new Bundle();
    bundle.putSerializable(ProfileFollowingFragment.USER_ARG, following);

    navController.navigate(R.id.navigation_profile_following, bundle);
  }

  /**
   * Opens PostFragment with provided trip and owns PostDetails and PostNotes Fragments.
   *
   * @param trip trip to open.
   */
  public void navigateToPostPage(Trip trip) {
    Bundle bundle = new Bundle();
    bundle.putSerializable(FeedAdapter.POST_ARG, trip);

    navController.navigate(R.id.navigation_post, bundle);
  }

  /**
   * Opens PostEditorFragment with provided trip.
   *
   * @param trip trip to open.
   */
  public void navigateToPostEditorPage(Trip trip) {
    Bundle bundle = new Bundle();
    bundle.putSerializable(FeedAdapter.POST_ARG, trip);

    navController.navigate(R.id.navigation_post_editor, bundle);
  }

  /**
   * Navigate to previous fragment in current graph.
   */
  public void navigateUp() {
    navController.navigateUp();
  }

  /**
   * Opens PlanTripFragment.
   */
  public void navigateToPlanTrip() {
    navController.navigate(R.id.navigation_plan_trip);
  }
}
