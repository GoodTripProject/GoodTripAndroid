package ru.hse.goodtrip;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import ru.hse.goodtrip.databinding.ActivityMainBinding;
import ru.hse.goodtrip.navigation.GtNavigationGraphMain;

public class MainActivity extends AppCompatActivity {

  private ActivityMainBinding binding;
  private GtNavigationGraphMain navigationGraph;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());
    navigationGraph = new GtNavigationGraphMain(this, binding);

    if (isUserLoggedIn()) { // TODO: move logic somewhere
      navigationGraph.navigateToLogin();
    }
  }

  public GtNavigationGraphMain getNavigationGraph() {
    return navigationGraph;
  }

  public boolean isUserLoggedIn() {
    return true;
  }

  @Override
  public boolean onSupportNavigateUp() {
    navigationGraph.navigateUp();
    return true;
  }

}