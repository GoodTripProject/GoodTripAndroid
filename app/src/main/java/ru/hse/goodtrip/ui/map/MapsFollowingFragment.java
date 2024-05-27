package ru.hse.goodtrip.ui.map;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
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
import ru.hse.goodtrip.data.model.User;
import ru.hse.goodtrip.data.model.trips.City;
import ru.hse.goodtrip.data.model.trips.CountryVisit;
import ru.hse.goodtrip.data.model.trips.Trip;
import ru.hse.goodtrip.network.trips.model.TripState;
import ru.hse.goodtrip.ui.profile.following.ProfileFollowingFragment;

public class MapsFollowingFragment extends Fragment {

  private MapsFollowingViewModel mapsFollowingViewModel;
  private final OnMapReadyCallback callback = googleMap -> {
    showTripPaths(googleMap);
    CustomInfoWindowAdapter customInfoWindowAdapter = new CustomInfoWindowAdapter(
        (MainActivity) requireActivity());
    googleMap.setInfoWindowAdapter(customInfoWindowAdapter);
    googleMap.setOnMarkerClickListener(customInfoWindowAdapter::markerClickListener);
    googleMap.setOnInfoWindowClickListener(customInfoWindowAdapter::infoWindowClickListener);
  };
  private User user;

  @Override
  public void onResume() {
    super.onResume();
    ((MainActivity) requireActivity()).getSupportActionBar().show();
    ((MainActivity) requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    ((MainActivity) requireActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
  }

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
    mapsFollowingViewModel = new ViewModelProvider(this).get(MapsFollowingViewModel.class);

    Bundle args = getArguments();
    if (args != null) {
      this.user = (User) args.get(ProfileFollowingFragment.USER_ARG);
      mapsFollowingViewModel.setUser(user);
    }
  }

  private void showTripPaths(GoogleMap googleMap) {
    ExecutorService service = Executors.newSingleThreadExecutor();
    service.submit(() -> {
      mapsFollowingViewModel.refreshMarks();
      for (Trip trip : mapsFollowingViewModel.getMarks()) {
        if (!trip.getTripState().equals(TripState.PUBLISHED)) {
          continue;
        }

        Log.d("asd", "зашел");
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
                assert mark != null; //TODO: ??

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