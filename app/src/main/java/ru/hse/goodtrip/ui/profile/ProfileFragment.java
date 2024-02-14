package ru.hse.goodtrip.ui.profile;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import ru.hse.goodtrip.R;
import ru.hse.goodtrip.databinding.FragmentPlacesBinding;
import ru.hse.goodtrip.databinding.FragmentProfileBinding;
import ru.hse.goodtrip.ui.places.PlacesFragment;
import ru.hse.goodtrip.ui.places.PlacesViewModel;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private ProfileViewModel mViewModel;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    private FragmentProfileBinding binding;

    Button backButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ru.hse.goodtrip.ui.places.PlacesViewModel placesViewModel =
                new ViewModelProvider(this).get(PlacesViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.goBackButton.setOnClickListener(this);

        final TextView textView = binding.textProfile;
        placesViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    // TODO: Does not go to previous fragment
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.goBackButton) {
            goBack();
        } else {
            // some other buttons
        }
    }

    public void goBack() {
        getParentFragmentManager().popBackStack();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}