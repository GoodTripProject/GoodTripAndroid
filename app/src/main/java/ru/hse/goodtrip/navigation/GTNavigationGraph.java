package ru.hse.goodtrip.navigation;

import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import ru.hse.goodtrip.MainActivity;
import ru.hse.goodtrip.R;
import ru.hse.goodtrip.databinding.ActivityMainBinding;

public class GTNavigationGraph extends NavHostFragment {

  private final MainActivity activity;
  private final ActivityMainBinding binding;
  private NavController navController;

  public GTNavigationGraph(MainActivity activity, ActivityMainBinding binding) {
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

    AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
        R.id.navigation_feed, R.id.navigation_map, R.id.navigation_places, R.id.navigation_profile)
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
    navController.navigate(R.id.navigation_login);
  }

  public void navigateUp() {
    navController.popBackStack();
  }

  public void navigateToPlanTrip() {
    activity.getSupportActionBar().show();
    navController.navigate(R.id.navigation_plan_trip);
  }
}
