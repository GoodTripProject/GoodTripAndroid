package ru.hse.goodtrip.ui.map;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import ru.hse.goodtrip.MainActivity;
import ru.hse.goodtrip.R;
import ru.hse.goodtrip.data.model.trips.City;
import ru.hse.goodtrip.data.model.trips.CountryVisit;
import ru.hse.goodtrip.data.model.trips.Trip;
import ru.hse.goodtrip.network.trips.model.TripState;

/**
 * MapsFragment.
 */
public class MapsFragment extends Fragment {

  MapsViewModel mapsViewModel;
  private final OnMapReadyCallback callback = googleMap -> {
    showTripPaths(googleMap);
    CustomInfoWindowAdapter customInfoWindowAdapter = new CustomInfoWindowAdapter(
        (MainActivity) requireActivity());
    googleMap.setInfoWindowAdapter(customInfoWindowAdapter);
    googleMap.setOnMarkerClickListener(customInfoWindowAdapter::markerClickListener);
    googleMap.setOnInfoWindowClickListener(customInfoWindowAdapter::infoWindowClickListener);
  };

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_maps, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(
        R.id.map);
    if (mapFragment != null) {
      mapFragment.getMapAsync(callback);
    }

    mapsViewModel = new ViewModelProvider(this).get(MapsViewModel.class);
  }

  /**
   * Show marks and paths of trip in map.
   *
   * @param googleMap googleMap.
   */
  private void showTripPaths(GoogleMap googleMap) {
    ExecutorService service = Executors.newSingleThreadExecutor();
    service.submit(() -> {
      mapsViewModel.refreshMarks();
      for (Trip trip : mapsViewModel.getMarks()) {
        if (!trip.getTripState().equals(TripState.PUBLISHED)) {
          continue;
        }

        Handler handler = Handler.createAsync(Looper.getMainLooper());
        handler.post(() -> {
          if (!trip.getCountries().isEmpty()) {
            PolylineOptions path = new PolylineOptions();
            for (CountryVisit country : trip.getCountries()) {
              for (City city : country.getVisitedCities()) {
                LatLng marker = new LatLng(city.getCoordinates().getLatitude(),
                    city.getCoordinates().getLongitude());
                Marker mark = googleMap.addMarker(
                    new MarkerOptions().position(marker).title(trip.getTitle()));
                assert mark != null;

                mark.setTag(trip);
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(marker));
                path.add(marker);
              }
            }
            path.color(Color.RED).width(5);
            googleMap.addPolyline(path);
          }
        });
      }
    });
  }
}