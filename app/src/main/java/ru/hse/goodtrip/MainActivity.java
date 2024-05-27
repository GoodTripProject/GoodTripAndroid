package ru.hse.goodtrip;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.FirebaseApp;
import lombok.Getter;
import ru.hse.goodtrip.databinding.ActivityMainBinding;
import ru.hse.goodtrip.navigation.GtNavigationGraphMain;
import ru.hse.goodtrip.network.NetworkManager;

/**
 * Main Activity of app.
 */
@Getter
public class MainActivity extends AppCompatActivity {

  private GtNavigationGraphMain navigationGraph;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    FirebaseApp.initializeApp(this);
    String urlApi = getResources().getString(R.string.URL_API);
    NetworkManager.setBaseUrl(urlApi);
    ru.hse.goodtrip.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(
        getLayoutInflater());
    setContentView(binding.getRoot());
    navigationGraph = new GtNavigationGraphMain(this, binding);

    navigationGraph.navigateToLogin();
  }

  @Override
  public boolean onSupportNavigateUp() {
    navigationGraph.navigateUp();
    return true;
  }

}