package ru.hse.goodtrip.ui.profile.mytrips;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import ru.hse.goodtrip.databinding.FragmentMyTripsBinding;

/**
 * MyTripsFragment.
 */
public class MyTripsFragment extends Fragment {

  private MyTripsViewModel myTripsViewModel;
  private FragmentMyTripsBinding binding;

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    myTripsViewModel = new ViewModelProvider(this).get(MyTripsViewModel.class);
    binding = FragmentMyTripsBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    LinearLayout trips = binding.trips;
    for (Trip trip : TripRepository.getInstance().getTrips()) {
      ItemTripProfileBinding tripProfileBinding = ItemTripProfileBinding.inflate(
          getLayoutInflater());
      tripProfileBinding.tripTitle.setText(trip.getTitle());

      tripProfileBinding.tripDuration.setText(
          getDuration(trip.getStartTripDate(), trip.getEndTripDate(), "dd.MM.yyyy"));
      tripProfileBinding.tripCard.setOnClickListener(
          v -> ((MainActivity) requireActivity()).getNavigationGraph()
              .navigateToPostEditorPage(trip));
      trips.addView(tripProfileBinding.getRoot());
    }
  }

}