package ru.hse.goodtrip.ui.map;

import static ru.hse.goodtrip.ui.trips.feed.utils.Utils.getDuration;
import static ru.hse.goodtrip.ui.trips.feed.utils.Utils.setImageByUrl;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;
import ru.hse.goodtrip.MainActivity;
import ru.hse.goodtrip.R;
import ru.hse.goodtrip.data.model.trips.Trip;

class CustomInfoWindowAdapter implements InfoWindowAdapter {

  private final View contents;
  private final View window;
  private final MainActivity context;

  CustomInfoWindowAdapter(MainActivity context) {
    this.context = context;
    window = context.getLayoutInflater().inflate(R.layout.custom_info_window, null);
    contents = context.getLayoutInflater().inflate(R.layout.custom_info_contents, null);
  }

  @Override
  public View getInfoWindow(@NonNull Marker marker) {
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
    Trip trip = (Trip) marker.getTag();
    if (trip != null) {
      titleTextView.setText(trip.getTitle());

      snippetTextView.setText(
          getDuration(trip.getStartTripDate(), trip.getEndTripDate(), "dd.MM.yyyy"));

      setImageByUrl(tripPhotoView, trip.getMainPhotoUrl(), R.drawable.kazantip);
    }

    titleTextView.setText(marker.getTitle());
  }

  public boolean markerClickListener(Marker m) {
    if (m.isInfoWindowShown()) {
      m.hideInfoWindow();
    } else {
      m.showInfoWindow();
    }
    Log.d("map", "marker clicked");
    return true;
  }

  public void infoWindowClickListener(Marker marker) {
    Log.d("map", "infowindow clicked");

    Trip trip = (Trip) marker.getTag();
    if (trip != null) {
      context.getNavigationGraph().navigateToPostPageExternal(trip);
    }
  }
}
