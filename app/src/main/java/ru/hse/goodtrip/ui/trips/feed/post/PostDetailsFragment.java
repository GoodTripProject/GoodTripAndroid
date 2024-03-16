package ru.hse.goodtrip.ui.trips.feed.post;

import static ru.hse.goodtrip.ui.trips.feed.utils.Utils.getDateFormatted;
import static ru.hse.goodtrip.ui.trips.feed.utils.Utils.getDuration;
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
import androidx.lifecycle.ViewModelProvider;
import java.util.ArrayList;
import java.util.List;
import ru.hse.goodtrip.MainActivity;
import ru.hse.goodtrip.R;
import ru.hse.goodtrip.data.model.trips.City;
import ru.hse.goodtrip.data.model.trips.CountryVisit;
import ru.hse.goodtrip.data.model.trips.Trip;
import ru.hse.goodtrip.databinding.FragmentPostDetailsBinding;

/**
 * PostDetailsFragment.
 **/
public class PostDetailsFragment extends Fragment {

  private PostViewModel postViewModel;
  private FragmentPostDetailsBinding binding;
  private Trip trip;

  @Override
  public void onResume() {
    super.onResume();
    ((MainActivity) requireActivity()).getSupportActionBar().show();
    ((MainActivity) requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    ((MainActivity) requireActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
    requireActivity().findViewById(R.id.bottomToolsBar).setVisibility(View.GONE);
    requireActivity().findViewById(R.id.bottomToolsBarPost).setVisibility(View.VISIBLE);
  }


  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    binding = FragmentPostDetailsBinding.inflate(inflater, container, false);
    postViewModel = new ViewModelProvider(requireActivity()).get(PostViewModel.class);
    trip = postViewModel.getTrip().getValue();
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    String dateFormat = "dd.MM.yyyy";

    setImageByUrl(binding.profileImageView, trip.getUser().getMainPhotoUrl(),
        R.drawable.baseline_account_circle_24
    );
    setImageByUrl(binding.postImageView, trip.getMainPhotoUrl(), R.drawable.kazantip);
    binding.budgetLabel.setText(Integer.toString(trip.getMoneyInUsd()));
    binding.postTitle.setText(trip.getTitle());
    binding.profileNameText.setText(trip.getUser().getDisplayName());
    binding.tripDuration.setText(
        getDuration(trip.getStartTripDate(), trip.getEndTripDate(), dateFormat));
    binding.dateOfPublication.setText(
        getDateFormatted(trip.getTimeOfPublication(), dateFormat));

    List<String> cities = new ArrayList<>();
    for (CountryVisit country : trip.getCountries()) {
      for (City city : country.getVisitedCities()) {
        cities.add(city.getName());
      }
      addCountryView(country.getCountry().getName(), cities);
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