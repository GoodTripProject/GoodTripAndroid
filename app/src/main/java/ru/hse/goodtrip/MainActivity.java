package ru.hse.goodtrip;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import ru.hse.goodtrip.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

  Toolbar mActionBar;
  private ActivityMainBinding binding;

  NavController mNavController;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());
    setupNavigation();

    if (isUserLoggedIn()) { // TODO: move logic somewhere
      mNavController.navigate(R.id.navigation_login);
    }
  }

  public boolean isUserLoggedIn() {
    return true;
  }

  // TODO: move to another class
  public void setupNavigation() {
    mActionBar = (Toolbar) findViewById(R.id.my_toolbar);
    setSupportActionBar(mActionBar);
    mActionBar.hideOverflowMenu();

    mNavController = Navigation.findNavController(this, R.id.nav_host_fragment_container);
    NavigationUI.setupWithNavController(binding.bottomNavigationView, mNavController);
  }
}