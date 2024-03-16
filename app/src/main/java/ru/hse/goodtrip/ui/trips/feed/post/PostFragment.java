package ru.hse.goodtrip.ui.trips.feed.post;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import ru.hse.goodtrip.MainActivity;
import ru.hse.goodtrip.R;
import ru.hse.goodtrip.data.model.trips.Trip;
import ru.hse.goodtrip.databinding.FragmentPostBinding;
import ru.hse.goodtrip.ui.trips.feed.FeedAdapter;

/**
 * Shared layout for PostDetailsFragment and PostNotesFragment. Provide transferred Trip to
 * ViewModel that shared between Details and Notes. Has his own NavHostContainer to navigate between
 * both of them.
 */
public class PostFragment extends Fragment {

  PostViewModel postViewModel;
  FragmentPostBinding binding;

  @Override
  public void onResume() {
    super.onResume();
    ((MainActivity) requireActivity()).getSupportActionBar().show();
    ((MainActivity) requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    ((MainActivity) requireActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
    requireActivity().findViewById(R.id.bottomToolsBar).setVisibility(View.GONE);
    requireActivity().findViewById(R.id.bottomToolsBarPost).setVisibility(View.VISIBLE);
  }

  @Override
  public void onStop() {
    super.onStop();
    ((MainActivity) requireActivity()).getSupportActionBar().hide();
    requireActivity().findViewById(R.id.bottomToolsBar).setVisibility(View.VISIBLE);
    requireActivity().findViewById(R.id.bottomToolsBarPost).setVisibility(View.GONE);
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    binding = FragmentPostBinding.inflate(inflater, container, false);
    Bundle args = getArguments();
    if (args != null) {
      Trip trip = (Trip) args.get(FeedAdapter.POST_ARG);
      postViewModel = new ViewModelProvider(requireActivity()).get(PostViewModel.class);
      postViewModel.setTrip(trip);
    }

    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    NavController navController = Navigation.findNavController(requireActivity(),
        R.id.nav_host_fragment_container_post);

    BottomNavigationView bottomNavigationView = requireActivity().findViewById(
        R.id.bottom_navigation_view_post);

    AppBarConfiguration appBarConfigurationPost = new AppBarConfiguration.Builder(
        R.id.navigation_post_details, R.id.navigation_post_notes)
        .build();

    NavigationUI.setupWithNavController(bottomNavigationView, navController);
    NavigationUI.setupActionBarWithNavController((MainActivity) requireActivity(), navController,
        appBarConfigurationPost);

    NavigationUI.setupWithNavController(bottomNavigationView, navController);

    navController.navigate(R.id.navigation_post_details);
  }
}