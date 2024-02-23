package ru.hse.goodtrip;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import ru.hse.goodtrip.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

  Toolbar actionBar;
  NavController navController;
  private ActivityMainBinding binding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());
    setupNavigation();

    if (isUserLoggedIn()) { // TODO: move logic somewhere
      navController.navigate(R.id.navigation_login);
    }
  }

  public boolean isUserLoggedIn() {
    return true;
  }

  /**
   * TODO: move to another class
   * Setup navController.
   */
  public void setupNavigation() {
    actionBar = findViewById(R.id.my_toolbar);
    setSupportActionBar(actionBar);
    getSupportActionBar().hide();

    navController = Navigation.findNavController(this, R.id.nav_host_fragment_container);
    NavigationUI.setupWithNavController(binding.bottomNavigationView, navController);
  }
}