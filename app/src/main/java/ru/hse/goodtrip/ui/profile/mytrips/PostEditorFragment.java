package ru.hse.goodtrip.ui.profile.mytrips;

import static android.view.View.GONE;
import static ru.hse.goodtrip.ui.trips.feed.utils.Utils.setImageByUrl;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import ru.hse.goodtrip.MainActivity;
import ru.hse.goodtrip.R;
import ru.hse.goodtrip.data.model.trips.City;
import ru.hse.goodtrip.data.model.trips.CountryVisit;
import ru.hse.goodtrip.data.model.trips.Note;
import ru.hse.goodtrip.data.model.trips.Trip;
import ru.hse.goodtrip.databinding.FragmentPostEditorBinding;
import ru.hse.goodtrip.databinding.ItemNoteBinding;
import ru.hse.goodtrip.ui.trips.feed.FeedAdapter;

/**
 * A post editor fragment.
 */
public class PostEditorFragment extends Fragment {

  FragmentPostEditorBinding binding;
  Trip trip;

  @Override
  public void onResume() {
    super.onResume();
    Objects.requireNonNull(((MainActivity) requireActivity()).getSupportActionBar())
        .setDisplayHomeAsUpEnabled(true);
    Objects.requireNonNull(((MainActivity) requireActivity()).getSupportActionBar())
        .setDisplayShowHomeEnabled(true);
    requireActivity().findViewById(R.id.bottomToolsBar).setVisibility(View.GONE);
  }

  @Override
  public void onStop() {
    super.onStop();
    Objects.requireNonNull(((MainActivity) requireActivity()).getSupportActionBar())
        .setDisplayHomeAsUpEnabled(true);
    Objects.requireNonNull(((MainActivity) requireActivity()).getSupportActionBar())
        .setDisplayShowHomeEnabled(true);
    requireActivity().findViewById(R.id.bottomToolsBar).setVisibility(View.VISIBLE);
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    binding = FragmentPostEditorBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    String dateFormat = "dd.MM.yyyy";

    Bundle args = getArguments();
    if (args != null) {
      this.trip = (Trip) args.get(FeedAdapter.POST_ARG);
    }
    if (trip != null) {
      setImageByUrl(binding.postImageView, trip.getMainPhotoUrl(), R.drawable.kazantip);
      binding.budgetLabel.setText(Integer.toString(trip.getMoneyInUsd()));
      binding.postTitle.setText(trip.getTitle());

      List<String> cities = new ArrayList<>();
      for (CountryVisit country : trip.getCountries()) {
        for (City city : country.getVisitedCities()) {
          cities.add(city.getName());
        }
        addCountryView(country.getCountry().getName(), cities);
      }

      LinearLayout notes = binding.notes;
      boolean flag = false; // TODO: first note with image for testing
      for (Note note : trip.getNotes()) {
        ItemNoteBinding noteBinding = ItemNoteBinding.inflate(getLayoutInflater());
        noteBinding.noteHeadline.setText(note.getHeadline());
        if (!flag) {
          setImageByUrl(noteBinding.noteImageView, note.getPhotoUrl(), R.drawable.kazantip);
          flag = true;
        } else if (note.getPhotoUrl() != null && !note.getPhotoUrl().trim().isEmpty()) {
          setImageByUrl(noteBinding.noteImageView, note.getPhotoUrl());
        } else {
          noteBinding.imageContainer.setVisibility(GONE);
        }
        noteBinding.noteText.setText(note.getNote());
        noteBinding.placeName.setText(note.getPlace().getName());

        notes.addView(noteBinding.getRoot());
      }
    }
  }

  private void addCountryView(String country, List<String> cities) {
    View countryView = LayoutInflater.from(requireContext())
        .inflate(R.layout.item_country, null);
    TextView countryTitle = countryView.findViewById(R.id.titleCountry);
    countryTitle.setText(country);
    LinearLayout citiesLayout = countryView.findViewById(R.id.cities);
    for (String city : cities) {
      View cityView = LayoutInflater.from(requireContext())
          .inflate(R.layout.item_city, null);
      ((TextView) cityView.findViewById(R.id.cityelement)).setText(city);
      citiesLayout.addView(cityView);
    }
    binding.route.addView(countryView);
  }

}