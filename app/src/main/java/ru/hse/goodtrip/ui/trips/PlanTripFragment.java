package ru.hse.goodtrip.ui.trips;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import ru.hse.goodtrip.R;

public class PlanTripFragment extends Fragment {

  private PlanTripViewModel mViewModel;

  public static PlanTripFragment newInstance() {
    return new PlanTripFragment();
  }

  @Override
  public void onResume() {
    super.onResume();
    requireActivity().findViewById(R.id.bottomToolsBar).setVisibility(View.INVISIBLE);
  }

  @Override
  public void onStop() {
    super.onStop();
    requireActivity().findViewById(R.id.bottomToolsBar).setVisibility(View.VISIBLE);
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_plan_trip, container, false);
  }

}