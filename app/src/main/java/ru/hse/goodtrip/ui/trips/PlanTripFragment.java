package ru.hse.goodtrip.ui.trips;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    final EditText travelNameEditText = binding.travelNameEditText;
    final EditText arrivalDateEditText = binding.arrivalDateEditText;
    final EditText departureDateEditText = binding.departureDateEditText;
    final Button saveButton = binding.saveButton;
    final Button addCountry = binding.addCountry;
    final EditText moneyEditText = binding.budgetEditText;
    saveButton.setOnClickListener(v -> {
      mViewModel.createTrip(travelNameEditText.getText().toString(),new ArrayList<>(),departureDateEditText.getText().toString(),arrivalDateEditText.getText().toString(),new byte[5],moneyEditText.getText().toString(), new TreeSet<>(),new ArrayList<>());
      if (Objects.requireNonNull(mViewModel.getPlanTripFormState().getValue()).isDataValid()){
        updateUI();
      }
      else{
        Toast toast = Toast.makeText(requireContext(),Objects.requireNonNull(mViewModel.getPlanTripFormState().getValue().getError()) ,Toast.LENGTH_LONG);
        toast.show();
      }
    });
  }

  private void updateUI(){
      Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_container)
              .popBackStack();
  }

}