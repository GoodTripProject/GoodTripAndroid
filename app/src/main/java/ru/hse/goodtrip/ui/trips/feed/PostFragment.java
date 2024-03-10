package ru.hse.goodtrip.ui.trips.feed;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import ru.hse.goodtrip.data.model.trips.Trip;
import ru.hse.goodtrip.databinding.FragmentPostBinding;

/**
 * Post screen.
 */
public class PostFragment extends Fragment {

  private FragmentPostBinding binding;
  private Trip trip;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {

    }
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    binding = FragmentPostBinding.inflate(inflater, container, false);

    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    // TODO
  }

}