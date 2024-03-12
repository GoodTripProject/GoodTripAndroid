package ru.hse.goodtrip.ui.trips.feed.post;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import ru.hse.goodtrip.MainActivity;
import ru.hse.goodtrip.R;
import ru.hse.goodtrip.data.model.trips.Trip;
import ru.hse.goodtrip.databinding.FragmentPostDetailsBinding;

/**
 * Post screen.
 */
public class PostDetailsFragment extends Fragment {

  private PostViewModel postViewModel;
  private FragmentPostDetailsBinding binding;
  private Trip trip;

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
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    binding = FragmentPostDetailsBinding.inflate(inflater, container, false);
    Bundle args = getArguments();
    if (args != null) {
      Trip trip = (Trip) args.get("post");
      Log.d("ASd", trip.getTitle());
      PostViewModelFactory factory = new PostViewModelFactory(trip);
      postViewModel = new ViewModelProvider(this, factory).get(PostViewModel.class);
    }

//    BottomNavigationView bottomNavigationView = requireActivity().findViewById(
//        R.id.bottom_post_nav_view);
//
//    AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//        R.id.navigation_post_details, R.id.navigation_post_notes)
//        .build();
//
//    NavController navController = Navigation.findNavController(requireActivity(),
//        R.id.nav_host_fragment_container);
//    NavigationUI.setupWithNavController(bottomNavigationView, navController);
//    NavigationUI.setupActionBarWithNavController((MainActivity) requireActivity(), navController,
//        appBarConfiguration);
//
//    NavigationUI.setupWithNavController(bottomNavigationView, navController);

    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    // TODO
  }

}