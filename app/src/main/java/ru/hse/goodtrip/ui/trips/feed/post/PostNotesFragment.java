package ru.hse.goodtrip.ui.trips.feed.post;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import ru.hse.goodtrip.MainActivity;
import ru.hse.goodtrip.R;

public class PostNotesFragment extends Fragment {

  @Override
  public void onResume() {
    super.onResume();
    ((MainActivity) requireActivity()).getSupportActionBar().show();
    requireActivity().findViewById(R.id.bottomToolsBar).setVisibility(View.GONE);
    requireActivity().findViewById(R.id.bottom_post_nav_view).setVisibility(View.VISIBLE);
  }

  @Override
  public void onStop() {
    super.onStop();
    ((MainActivity) requireActivity()).getSupportActionBar().hide();
    requireActivity().findViewById(R.id.bottomToolsBar).setVisibility(View.VISIBLE);
    requireActivity().findViewById(R.id.bottom_post_nav_view).setVisibility(View.GONE);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {


    return inflater.inflate(R.layout.fragment_post_notes, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    // TODO
  }
}