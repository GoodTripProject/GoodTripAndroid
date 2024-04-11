package ru.hse.goodtrip.ui.trips.feed.post;

import static android.view.View.GONE;
import static ru.hse.goodtrip.ui.trips.feed.utils.Utils.setImageByUrl;

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
import ru.hse.goodtrip.R;
import ru.hse.goodtrip.data.model.trips.Note;
import ru.hse.goodtrip.data.model.trips.Trip;
import ru.hse.goodtrip.databinding.FragmentPostNotesBinding;
import ru.hse.goodtrip.databinding.ItemNoteBinding;

/**
 * PostNotesFragment.
 */
public class PostNotesFragment extends Fragment {

  PostViewModel postViewModel;
  FragmentPostNotesBinding binding;
  Trip trip;

  @Override
  public void onResume() {
    super.onResume();
    ((MainActivity) requireActivity()).getSupportActionBar().show();
    ((MainActivity) requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    ((MainActivity) requireActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
    requireActivity().findViewById(R.id.bottomToolsBar).setVisibility(GONE);
    requireActivity().findViewById(R.id.bottomToolsBarPost).setVisibility(View.VISIBLE);
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    binding = FragmentPostNotesBinding.inflate(inflater, container, false);
    postViewModel = new ViewModelProvider(requireActivity()).get(PostViewModel.class);
    trip = postViewModel.getTrip().getValue();
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    LinearLayout notes = binding.notes;
    if (trip.getNotes().isEmpty()) {
      binding.noNotes.setVisibility(View.VISIBLE);
    } else {
      int i = 0;
      for (Note note : trip.getNotes()) {
        i++;
        ItemNoteBinding noteBinding = ItemNoteBinding.inflate(getLayoutInflater());
        noteBinding.noteHeadline.setText(note.getHeadline());
        if (i == 2) {
          noteBinding.imageContainer.setVisibility(GONE);
        } else {
          if (note.getPhotoUrl() != null && !note.getPhotoUrl().trim().isEmpty()) {
            setImageByUrl(noteBinding.noteImageView, note.getPhotoUrl());
          } else {
            noteBinding.imageContainer.setVisibility(GONE);
          }
        }
        noteBinding.noteText.setText(note.getNote());
        noteBinding.placeName.setText(note.getPlace().getName());

        notes.addView(noteBinding.getRoot());
      }
    }
  }
}