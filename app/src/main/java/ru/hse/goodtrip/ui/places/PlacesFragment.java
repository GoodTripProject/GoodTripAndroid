package ru.hse.goodtrip.ui.places;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.hse.goodtrip.databinding.FragmentPlacesBinding;

public class PlacesFragment extends Fragment {

    private PlacesViewModel mViewModel;
    private FragmentPlacesBinding binding;

    public static PlacesFragment newInstance() {
        return new PlacesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ru.hse.goodtrip.ui.places.PlacesViewModel placesViewModel =
                new ViewModelProvider(this).get(PlacesViewModel.class);

        binding = FragmentPlacesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textPlaces;
        placesViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}