package ru.hse.goodtrip.ui.trips;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.hse.goodtrip.R;

public class AddTrip extends Fragment {

    private AddTripViewModel mViewModel;

    public static AddTrip newInstance() {
        return new AddTrip();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_trip, container, false);
    }

}