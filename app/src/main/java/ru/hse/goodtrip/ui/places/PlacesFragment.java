package ru.hse.goodtrip.ui.places;

import static ru.hse.goodtrip.ui.trips.feed.utils.Utils.setImageByUrl;

import android.Manifest;
import android.Manifest.permission;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import java.util.ArrayList;
import java.util.List;
import ru.hse.goodtrip.R;
import ru.hse.goodtrip.databinding.FragmentPlacesBinding;
import ru.hse.goodtrip.databinding.ItemPlaceBinding;
import ru.hse.goodtrip.network.places.model.PlaceResponse;

public class PlacesFragment extends Fragment {

  private FusedLocationProviderClient fusedLocationClient;

  private PlacesViewModel placesViewModel;
  private FragmentPlacesBinding binding;

  private static PlaceResponse place = new PlaceResponse("yevpatoriya", 10, 10, null, 5, "10");
  private List<PlaceResponse> places;

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater,
      ViewGroup container, Bundle savedInstanceState) {
    placesViewModel =
        new ViewModelProvider(this).get(PlacesViewModel.class);
    binding = FragmentPlacesBinding.inflate(inflater, container, false);

    fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    getNearsPlaces();
  }

  private void renderPlaces() {
    LinearLayout placesLayout = binding.places;

    for (PlaceResponse place : places) {
      ItemPlaceBinding placeBinding = ItemPlaceBinding.inflate(getLayoutInflater());
      placeBinding.placeName.setText(place.getName());
      placeBinding.placeRating.setText(String.valueOf(place.getRating()));
      placeBinding.distance.setText("100m"); // TODO
      setImageByUrl(placeBinding.placePhoto, place.getIcon(), R.drawable.kazantip);

      placesLayout.addView(placeBinding.getRoot());
    }
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }

  private void checkLocationPermission() {
    if (ActivityCompat.checkSelfPermission(requireActivity(), permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(requireActivity(), permission.ACCESS_COARSE_LOCATION)
        != PackageManager.PERMISSION_GRANTED) {
      if (ActivityCompat.shouldShowRequestPermissionRationale(
          requireActivity(),
          Manifest.permission.ACCESS_FINE_LOCATION
      )
      ) {
        requestLocationPermission();
      }
    }
  }

  private void requestLocationPermission() {
    ActivityCompat.requestPermissions(
        requireActivity(),
        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
        100
    );
  }

  private void getNearsPlaces() {
    checkLocationPermission();
    fusedLocationClient.getLastLocation()
        .addOnSuccessListener(requireActivity(), location -> {
          if (location != null) {

            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            //TODO: get places from API

            places = new ArrayList<>();
            places.add(place);
            places.add(place);

            renderPlaces();
          }
        }).addOnFailureListener(requireActivity(), location -> {
          Log.d("places", "=(");
        });
  }
}