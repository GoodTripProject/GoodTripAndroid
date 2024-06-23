package ru.hse.goodtrip.ui.trips.plantrip;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

/**
 * DialogWindows holder used as namespace for PlanTrip dialog windows.
 */
public class PlanTripDialogWindows {

  /**
   * Dialog add new destination screen.
   */
  public static class DialogAddNewDestinationFragment extends DialogFragment {

    ru.hse.goodtrip.databinding.FragmentDialogAddNewDestinationBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
      super.onCreateView(inflater, container, savedInstanceState);

      binding = ru.hse.goodtrip.databinding.FragmentDialogAddNewDestinationBinding.inflate(inflater,
          container, false);
      return binding.getRoot();
    }
  }

}
