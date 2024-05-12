package ru.hse.goodtrip.ui.trips.plantrip;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import ru.hse.goodtrip.MainActivity;
import ru.hse.goodtrip.R;
import ru.hse.goodtrip.data.UsersRepository;
import ru.hse.goodtrip.databinding.FragmentPlanTripBinding;
import ru.hse.goodtrip.ui.trips.plantrip.PlanTripDialogWindows.DialogAddNewDestinationFragment;

public class PlanTripFragment extends Fragment {

  private PlanTripViewModel planTripViewModel;
  private FragmentPlanTripBinding binding;

  @Override
  public void onResume() {
    super.onResume();
    ((MainActivity) requireActivity()).getSupportActionBar().show();
    ((MainActivity) requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    ((MainActivity) requireActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
    requireActivity().findViewById(R.id.bottomToolsBar).setVisibility(View.GONE);
  }

  @Override
  public void onStop() {
    super.onStop();
    ((MainActivity) requireActivity()).getSupportActionBar().hide();
    requireActivity().findViewById(R.id.bottomToolsBar).setVisibility(View.VISIBLE);
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding = FragmentPlanTripBinding.inflate(inflater, container, false);
    planTripViewModel = new ViewModelProvider(this).get(PlanTripViewModel.class);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    final EditText arrivalDateEditText = binding.arrivalDateEditText;
    final EditText departureDateEditText = binding.departureDateEditText;
    final Button saveButton = binding.saveButton;
    final ImageButton addCountry = binding.addCountry;
    DialogAddNewDestinationFragment dialog = new DialogAddNewDestinationFragment();
    addCountry.setOnClickListener(v -> showAddNewDestinationDialog(dialog));

    saveButton.setOnClickListener(this::saveTrip);
    departureDateEditText.setOnClickListener(this::selectDepartureDate);
    arrivalDateEditText.setOnClickListener(this::selectArrivalDate);
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

  private void setupAddCountryDialog(DialogAddNewDestinationFragment dialog) {
    final AutoCompleteTextView autoCompleteTextViewCountries = dialog.binding.enterCountryName;
    ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
        android.R.layout.select_dialog_item,
        PlanTripViewModel.getCountriesList().toArray(new String[0]));
    autoCompleteTextViewCountries.setThreshold(0);
    autoCompleteTextViewCountries.setAdapter(adapter);
    final Button addCountry = dialog.binding.popupAddCountry;
    final Button addCity = dialog.binding.popupAddCity;
    final ImageButton closeButton = dialog.binding.closeButton;
    LinearLayout citiesLayout = dialog.binding.addCities;
    addCountry.setOnClickListener(v -> {
      String country = autoCompleteTextViewCountries.getText().toString();
      List<String> cities = new ArrayList<>();
      for (int index = 0; index < citiesLayout.getChildCount(); ++index) {
        cities.add(
            ((TextView) (citiesLayout.getChildAt(index)).findViewById(
                R.id.enter_city_name)).getText().toString());
      }
      planTripViewModel.addCountry(country, cities);
      addCountryView(country, cities);
      dialog.dismiss();
    });

    addCity.setOnClickListener(v -> {
      View view = LayoutInflater.from(requireContext())
          .inflate(R.layout.item_add_city, null);
      view.setLayoutParams(
          new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
              LinearLayout.LayoutParams.WRAP_CONTENT));
      citiesLayout.addView(view);
      citiesLayout.refreshDrawableState();
    });

    closeButton.setOnClickListener(v -> {
      dialog.dismiss();
    });
  }

  private void selectDepartureDate(View view) {
    DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), R.style.DialogTheme,
        (datePicker, year, month, day) -> {
          String date = day + "." + month + "." + year;
          binding.departureDateEditText.setText(date);
        }, 2024, 4, 11);

    datePickerDialog.show();
  }

  private void selectArrivalDate(View view) {
    DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), R.style.DialogTheme,
        (datePicker, year, month, day) -> {
          String date = day + "." + month + "." + year;
          binding.arrivalDateEditText.setText(date);
        }, 2024, 4, 11);

    datePickerDialog.show();
  }

  private void showAddNewDestinationDialog(DialogAddNewDestinationFragment dialog) {
    dialog.show(getChildFragmentManager(), "dialog..."); // TODO
    getChildFragmentManager().executePendingTransactions();
    DisplayMetrics metrics = getResources().getDisplayMetrics();
    int width = metrics.widthPixels;
    dialog.getDialog().getWindow().setLayout((6 * width) / 7, LayoutParams.WRAP_CONTENT);
    setupAddCountryDialog(dialog);
  }

  private void saveTrip(View v) {
    planTripViewModel.createTrip(binding.travelNameEditText.getText().toString(),
        binding.departureDateEditText.getText().toString(),
        binding.arrivalDateEditText.getText().toString(),
        null, binding.budgetEditText.getText().toString(),
        new HashSet<>(),
        UsersRepository.getInstance().getLoggedUser()); // TODO
    if (Objects.requireNonNull(planTripViewModel.getPlanTripFormState().getValue())
        .isDataValid()) {
      navigateUp();
    } else {
      Toast toast = Toast.makeText(requireContext(),
          Objects.requireNonNull(planTripViewModel.getPlanTripFormState().getValue().getError()),
          Toast.LENGTH_LONG);
      toast.show();
    }
  }

  private void navigateUp() {
    ((MainActivity) requireActivity()).getNavigationGraph().navigateUp();
  }
}