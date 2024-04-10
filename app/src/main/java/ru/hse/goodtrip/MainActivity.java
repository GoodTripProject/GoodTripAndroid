package ru.hse.goodtrip;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import ru.hse.goodtrip.databinding.ActivityMainBinding;
import ru.hse.goodtrip.navigation.GtNavigationGraphMain;
import ru.hse.goodtrip.network.NetworkManager;

public class MainActivity extends AppCompatActivity {

  private ActivityMainBinding binding;
  private GtNavigationGraphMain navigationGraph;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    String urlApi = getResources().getString(R.string.URL_API);
    NetworkManager.setBaseUrl(urlApi);
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());
    navigationGraph = new GtNavigationGraphMain(this, binding);

    if (!isUserLoggedIn()) {
      navigationGraph.navigateToLogin();
    }
  }

  public GtNavigationGraphMain getNavigationGraph() {
    return navigationGraph;
  }

  public boolean isUserLoggedIn() {
    return false;
  }

  @Override
  public boolean onSupportNavigateUp() {
    navigationGraph.navigateUp();
    return true;
  }

}