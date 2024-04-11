package ru.hse.goodtrip.ui.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import ru.hse.goodtrip.R;
import ru.hse.goodtrip.data.model.trips.CountryVisit;
import ru.hse.goodtrip.data.model.trips.Trip;

public class MapsFragment extends Fragment {


  MapsViewModel mapsViewModel;
  private OnMapReadyCallback callback = googleMap -> {
    mapsViewModel.refreshMarks();
    for (Trip trip : mapsViewModel.getMarks()) {
      if (trip.getCountries().size() > 0) {
        CountryVisit firstDestination = trip.getCountries().get(0);
        int latitude = firstDestination.getCountry().getCoordinates().getLatitude();
        int longitude = firstDestination.getCountry().getCoordinates().getLongitude();
        LatLng tripMark = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions().position(tripMark).title(trip.getTitle()));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(tripMark));
      }
    }
  };

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_maps, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    SupportMapFragment mapFragment =
        (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
    if (mapFragment != null) {
      mapFragment.getMapAsync(callback);
    }

    mapsViewModel =
        new ViewModelProvider(this).get(MapsViewModel.class);
  }
}