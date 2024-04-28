package ru.hse.goodtrip.ui.trips.plantrip;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import ru.hse.goodtrip.databinding.FragmentDialogAddNewDestinationBinding;

/**
 * Dialog add new destination screen.
 */
public class DialogAddNewDestinationFragment extends DialogFragment {

  FragmentDialogAddNewDestinationBinding binding;

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);

    binding = FragmentDialogAddNewDestinationBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }
}