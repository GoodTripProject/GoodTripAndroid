package ru.hse.goodtrip.navigation;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import ru.hse.goodtrip.MainActivity;
import ru.hse.goodtrip.R;
import ru.hse.goodtrip.data.model.trips.Trip;
import ru.hse.goodtrip.databinding.ActivityMainBinding;
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
    activity.getSupportActionBar().hide();

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
    Log.d("SignUpFragment", "clicked!");
    navController.navigate(R.id.navigation_sign_up);
  }

  public void navigateToMainGraph() {
    activity.getSupportActionBar().hide();
    navController.navigate(R.id.main_navigation_graph);
  }

  public void navigateToPostPageFromMap(Trip trip) {
    activity.getNavigationGraph().navigateToMainGraph();
    activity.getNavigationGraph().navigateToPostPage(trip);
  }

  public void navigateToMyTrips() {
    navController.navigate(R.id.navigation_my_trips);
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

  public void navigateUp() {
    navController.navigateUp();
  }

  public void navigateToPlanTrip() {
    navController.navigate(R.id.navigation_plan_trip);
  }
}
