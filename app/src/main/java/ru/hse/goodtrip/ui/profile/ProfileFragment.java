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

public class ProfileFragment extends Fragment {

    private ProfileViewModel mViewModel;


    private FragmentProfileBinding binding;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textProfile;
        profileViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}