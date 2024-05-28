package ru.hse.goodtrip.ui.profile.mytrips;

import static ru.hse.goodtrip.network.trips.model.TripState.PUBLISHED;
import static ru.hse.goodtrip.ui.trips.feed.utils.Utils.getDuration;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import ru.hse.goodtrip.MainActivity;
import ru.hse.goodtrip.data.TripRepository;
import ru.hse.goodtrip.data.model.trips.Trip;
import ru.hse.goodtrip.databinding.FragmentMyTripsBinding;
import ru.hse.goodtrip.databinding.ItemTripProfileBinding;
import ru.hse.goodtrip.network.trips.model.TripState;

/**
 * MyTripsFragment.
 */
public class MyTripsFragment extends Fragment {

  private MyTripsViewModel myTripsViewModel;
  private FragmentMyTripsBinding binding;

  @Override
  public void onResume() {
    super.onResume();
    ((MainActivity) requireActivity()).getSupportActionBar().show();
    ((MainActivity) requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    ((MainActivity) requireActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
  }


  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    myTripsViewModel = new ViewModelProvider(this).get(MyTripsViewModel.class);
    binding = FragmentMyTripsBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  /**
   * From TripState to String.
   *
   * @param state trip state.
   * @return trip state in string.
   */
  private String stateToString(TripState state) {
    switch (state) {
      case PLANNED:
        return "Planned";
      case PUBLISHED:
        return "Published";
      case IN_PROCESS:
        return "IN_PROCESS";
      default:
        return null;
    }
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    renderTrips();
  }

  private void renderTrips() {
    LinearLayout trips = binding.trips;
    for (Trip trip : TripRepository.getInstance().getUserTrips()) {
      ItemTripProfileBinding tripProfileBinding = ItemTripProfileBinding.inflate(
          getLayoutInflater());
      tripProfileBinding.tripTitle.setText(trip.getTitle());
      tripProfileBinding.tripState.setText(stateToString(trip.getTripState()));
      tripProfileBinding.tripDuration.setText(
          getDuration(trip.getStartTripDate(), trip.getEndTripDate(), "dd.MM"));
      tripProfileBinding.tripCard.setOnClickListener(
          v -> {
            if (trip.getTripState().equals(PUBLISHED)) {
              ((MainActivity) requireActivity()).getNavigationGraph()
                  .navigateToPostPageExternal(trip);
            } else {
              ((MainActivity) requireActivity()).getNavigationGraph()
                  .navigateToPostEditorPage(trip);
            }
          });
      trips.addView(tripProfileBinding.getRoot());
    }
  }
}