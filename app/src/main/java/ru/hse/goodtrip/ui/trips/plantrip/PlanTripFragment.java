package ru.hse.goodtrip.ui.trips.plantrip;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import ru.hse.goodtrip.MainActivity;
import ru.hse.goodtrip.R;
import ru.hse.goodtrip.data.UsersRepository;
import ru.hse.goodtrip.databinding.FragmentPlanTripBinding;

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

  private void setupAddCountryPopup(View popupView, PopupWindow popupWindow) {
    binding.getRoot().setAlpha((float) 0.2);
    final AutoCompleteTextView autoCompleteTextViewCountries = popupView.findViewById(
        R.id.enter_country_name);
    ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
        android.R.layout.select_dialog_item,
        planTripViewModel.getCountriesList().toArray(new String[0]));
    autoCompleteTextViewCountries.setThreshold(0);
    autoCompleteTextViewCountries.setAdapter(adapter);
    final Button addCountry = popupView.findViewById(R.id.popup_add_country);
    final Button addCity = popupView.findViewById(R.id.popup_add_city);
    LinearLayout citiesLayout = popupView.findViewById(R.id.add_cities);
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
      popupWindow.dismiss();
      binding.getRoot().setAlpha((float) 1);
      setEditTexts(true);
    });

    addCity.setOnClickListener(v -> {
      View view = LayoutInflater.from(requireContext())
          .inflate(R.layout.item_add_city, null);
      view.setLayoutParams(
          new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
              LinearLayout.LayoutParams.WRAP_CONTENT));
      citiesLayout.addView(view);
      citiesLayout.refreshDrawableState();
      popupView.refreshDrawableState();
    });
  }

  private void setEditTexts(boolean enabled) {
    binding.travelNameEditText.setEnabled(enabled);
    binding.budgetEditText.setEnabled(enabled);
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

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    final EditText travelNameEditText = binding.travelNameEditText;
    final EditText arrivalDateEditText = binding.arrivalDateEditText;
    final EditText departureDateEditText = binding.departureDateEditText;
    final Button saveButton = binding.saveButton;
    final ImageButton addCountry = binding.addCountry;
    final EditText moneyEditText = binding.budgetEditText;
    LayoutInflater inflater = (LayoutInflater) requireContext().getSystemService(
        LAYOUT_INFLATER_SERVICE);
    View popupView = inflater.inflate(R.layout.popup_add_country_and_cities, null);
    final PopupWindow popupWindow = new PopupWindow(popupView,
        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
    popupWindow.setOutsideTouchable(false);
    addCountry.setOnClickListener(v -> {
      popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
      setupAddCountryPopup(popupView, popupWindow);
      setEditTexts(false);
    });
    saveButton.setOnClickListener(v -> {
      planTripViewModel.createTrip(travelNameEditText.getText().toString(),
          departureDateEditText.getText().toString(), arrivalDateEditText.getText().toString(),
          null, moneyEditText.getText().toString(), new HashSet<>(),
          UsersRepository.getInstance().getLoggedUser()); // TODO
      if (Objects.requireNonNull(planTripViewModel.getPlanTripFormState().getValue())
          .isDataValid()) {
        updateUi();
      } else {
        Toast toast = Toast.makeText(requireContext(),
            Objects.requireNonNull(planTripViewModel.getPlanTripFormState().getValue().getError()),
            Toast.LENGTH_LONG);
        toast.show();
      }
    });

    departureDateEditText.setOnClickListener(this::selectDepartureDate);
    arrivalDateEditText.setOnClickListener(this::selectArrivalDate);
  }

  private void updateUi() {
    ((MainActivity) requireActivity()).getNavigationGraph().navigateUp();
  }

}