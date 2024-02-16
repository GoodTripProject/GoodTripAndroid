package ru.hse.goodtrip;

import static android.app.PendingIntent.getActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.Objects;

import ru.hse.goodtrip.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    Toolbar mActionBar;
    private ActivityMainBinding binding;
    ImageButton profileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mActionBar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mActionBar);
        profileButton = findViewById(R.id.profile_button);


        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_feed, R.id.navigation_map, R.id.navigation_places)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);


        // process multiple buttons in future
        profileButton.setOnClickListener(v -> {
            profileButton.setVisibility(View.GONE);
            mActionBar.setNavigationOnClickListener(v1 -> {
                profileButton.setVisibility(View.VISIBLE);
                navController.popBackStack();
            });
            navController.navigate(R.id.navigation_profile);
        });


    }
}