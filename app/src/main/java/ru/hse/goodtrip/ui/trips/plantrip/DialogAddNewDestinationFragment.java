package ru.hse.goodtrip.ui.trips.plantrip;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import ru.hse.goodtrip.databinding.FragmentDialogAddNewDestinationBinding;

/**
 * Dialog add new destination screen.
 */
public class DialogAddNewDestinationFragment extends Fragment {

  private FragmentDialogAddNewDestinationBinding binding;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    binding = FragmentDialogAddNewDestinationBinding.inflate(inflater, container, false);

    return binding.getRoot();
  }
}