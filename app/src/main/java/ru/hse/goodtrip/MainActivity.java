package ru.hse.goodtrip;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import ru.hse.goodtrip.databinding.ActivityMainBinding;
import ru.hse.goodtrip.navigation.GtNavigationGraph;

public class MainActivity extends AppCompatActivity {

  private ActivityMainBinding binding;
  private GtNavigationGraph navigationGraph;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());
    navigationGraph = new GtNavigationGraph(this, binding);

    if (isUserLoggedIn()) { // TODO: move logic somewhere
      navigationGraph.navigateToLogin();
    }
  }

  public GtNavigationGraph getNavigationGraph() {
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