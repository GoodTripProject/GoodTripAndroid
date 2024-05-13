package ru.hse.goodtrip.ui.map;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import ru.hse.goodtrip.R;
import ru.hse.goodtrip.data.model.trips.City;
import ru.hse.goodtrip.data.model.trips.CountryVisit;
import ru.hse.goodtrip.data.model.trips.Trip;

public class MapsFragment extends Fragment {

  MapsViewModel mapsViewModel;
  private OnMapReadyCallback callback = googleMap -> {
    mapsViewModel.refreshMarks();

    for (Trip trip : mapsViewModel.getMarks()) {
      if (trip.getCountries().size() > 0) {
        PolylineOptions path = new PolylineOptions();
        for (CountryVisit country : trip.getCountries()) {
          for (City city : country.getVisitedCities()) {
            LatLng marker = new LatLng(city.getCoordinates().getLatitude(),
                city.getCoordinates().getLongitude());
            googleMap.addMarker(
                new MarkerOptions().position(marker).title(trip.getTitle()));
            path.add(marker);
          }
        }
        path.color(Color.RED).width(7);
        googleMap.addPolyline(path);
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