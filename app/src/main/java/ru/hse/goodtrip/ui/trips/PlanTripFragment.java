package ru.hse.goodtrip.ui.trips;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import java.util.ArrayList;
import java.util.Objects;
import java.util.TreeSet;
import ru.hse.goodtrip.R;
import ru.hse.goodtrip.databinding.FragmentPlanTripBinding;

public class PlanTripFragment extends Fragment {

  private PlanTripViewModel mViewModel;
  private FragmentPlanTripBinding binding;

  public static PlanTripFragment newInstance() {
    return new PlanTripFragment();
  }

  @Override
  public void onResume() {
    super.onResume();
    requireActivity().findViewById(R.id.bottomToolsBar).setVisibility(View.INVISIBLE);
  }

  @Override
  public void onStop() {
    super.onStop();
    requireActivity().findViewById(R.id.bottomToolsBar).setVisibility(View.VISIBLE);
  }


  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding = FragmentPlanTripBinding.inflate(inflater, container, false);
    mViewModel = new PlanTripViewModel();
    return binding.getRoot();
  }

  private void setupAddCountryPopup(View popupView, PopupWindow popupWindow) {
    binding.getRoot().setAlpha((float) 0.2);
    final AutoCompleteTextView autoCompleteTextViewCountries = popupView.findViewById(
        R.id.enter_country_name);
    ArrayAdapter<String> adapter = new ArrayAdapter<>
        (requireContext(), android.R.layout.select_dialog_item,
            mViewModel.getCountriesList().toArray(new String[0]));
    autoCompleteTextViewCountries.setThreshold(0);
    autoCompleteTextViewCountries.setAdapter(adapter);
    final Button addCountry = popupView.findViewById(R.id.popup_add_country);
    addCountry.setOnClickListener(v -> {

      mViewModel.addCountry();
      popupWindow.dismiss();
      binding.getRoot().setAlpha((float) 1);
      setEditTexts(true);
    });
    final Button addCity = popupView.findViewById(R.id.popup_add_city);
    LinearLayout citiesLayout = popupView.findViewById(R.id.add_cities);
    ConstraintLayout addCityLayout = citiesLayout.findViewById(R.id.add_city);
    addCity.setOnClickListener(v -> {

    });
  }

  private void setEditTexts(boolean enabled) {
    binding.travelNameEditText.setEnabled(enabled);
    binding.arrivalDateEditText.setEnabled(enabled);
    binding.departureDateEditText.setEnabled(enabled);
    binding.budgetEditText.setEnabled(enabled);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    final EditText travelNameEditText = binding.travelNameEditText;
    final EditText arrivalDateEditText = binding.arrivalDateEditText;
    final EditText departureDateEditText = binding.departureDateEditText;
    final Button saveButton = binding.saveButton;
    final Button addCountry = binding.addCountry;
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
      mViewModel.createTrip(travelNameEditText.getText().toString(), new ArrayList<>(),
          departureDateEditText.getText().toString(), arrivalDateEditText.getText().toString(),
          new byte[5], moneyEditText.getText().toString(), new TreeSet<>(), new ArrayList<>());
      if (Objects.requireNonNull(mViewModel.getPlanTripFormState().getValue()).isDataValid()) {
        updateUI();
      } else {
        Toast toast = Toast.makeText(requireContext(),
            Objects.requireNonNull(mViewModel.getPlanTripFormState().getValue().getError()),
            Toast.LENGTH_LONG);
        toast.show();
      }
    });
  }

  private void updateUI() {
    Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_container)
        .popBackStack();
  }

}