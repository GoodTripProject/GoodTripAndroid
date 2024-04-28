package ru.hse.goodtrip.ui.profile.mytrips;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import ru.hse.goodtrip.databinding.FragmentAddNewNoteDialogBinding;

public class PostEditorDialogWindows {

  /**
   * Dialog window for adding new note.
   */
  public static class AddNewNoteDialogFragment extends DialogFragment {

    FragmentAddNewNoteDialogBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
      super.onCreateView(inflater, container, savedInstanceState);

      binding = FragmentAddNewNoteDialogBinding.inflate(inflater, container, false);
      return binding.getRoot();
    }
  }

  /**
   * Dialog add new destination screen.
   */
  public static class AddNewDestinationDialogFragment extends DialogFragment {

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
