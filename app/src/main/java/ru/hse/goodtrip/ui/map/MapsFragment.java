package ru.hse.goodtrip.ui.map;

import static ru.hse.goodtrip.ui.trips.feed.utils.Utils.getDuration;
import static ru.hse.goodtrip.ui.trips.feed.utils.Utils.setImageByUrl;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
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

public class MapsFragment extends Fragment {

  MapsViewModel mapsViewModel;
  private final OnMapReadyCallback callback = googleMap -> {
    showTripPaths(googleMap);

    googleMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());
    googleMap.setOnMarkerClickListener(this::markerClickListener);
    googleMap.setOnInfoWindowClickListener(this::infoWindowClickListener);
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

  private boolean markerClickListener(Marker m) {
    if (m.isInfoWindowShown()) {
      m.hideInfoWindow();
    } else {
      m.showInfoWindow();
    }
    Log.d("map", "marker clicked");
    return true;
  }

  private void infoWindowClickListener(Marker marker) {
    Log.d("map", "infowindow clicked");

    Trip trip = (Trip) marker.getTag();
    if (trip != null) {
      ((MainActivity) requireActivity()).getNavigationGraph()
          .navigateToPostPageExternal(trip);
    }
  }

  private void showTripPaths(GoogleMap googleMap) {
    ExecutorService service = Executors.newSingleThreadExecutor();
    service.submit(() -> {
      mapsViewModel.refreshMarks();
      for (Trip trip : mapsViewModel.getMarks()) {
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

  class CustomInfoWindowAdapter implements InfoWindowAdapter {

    private final View contents;
    private final View window;

    CustomInfoWindowAdapter() {
      window = getLayoutInflater().inflate(R.layout.custom_info_window, null);
      contents = getLayoutInflater().inflate(R.layout.custom_info_contents, null);
    }

    @Override
    public View getInfoWindow(@NonNull Marker marker) {
//      render(marker, window);
      return null;
    }

    @Override
    public View getInfoContents(@NonNull Marker marker) {
      render(marker, contents);
      return contents;
    }

    private void render(Marker marker, View view) {
      TextView titleTextView = view.findViewById(R.id.title);
      TextView snippetTextView = view.findViewById(R.id.snippet);
      ImageView tripPhotoView = view.findViewById(R.id.tripImage);
      Button showTripButton = view.findViewById(R.id.showTripButton);
      Trip trip = (Trip) marker.getTag();
      if (trip != null) {
        titleTextView.setText(trip.getTitle());

        snippetTextView.setText(
            getDuration(trip.getStartTripDate(), trip.getEndTripDate(), "dd.MM.yyyy"));

        setImageByUrl(tripPhotoView, trip.getMainPhotoUrl(), R.drawable.kazantip);
//        showTripButton.setOnClickListener(
//            v -> );
      }

      titleTextView.setText(marker.getTitle());
    }
  }
}