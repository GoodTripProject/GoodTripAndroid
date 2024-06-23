package ru.hse.goodtrip.ui.map;

import static ru.hse.goodtrip.ui.trips.feed.utils.Utils.getDuration;
import static ru.hse.goodtrip.ui.trips.feed.utils.Utils.setImageByUrl;

import android.annotation.SuppressLint;
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

/**
 * CustomInfoWindowAdapter to show a InfoWindow when mark is clicked.
 */
class CustomInfoWindowAdapter implements InfoWindowAdapter {

  private static final String TAG = "MAP";
  private final View contents;
  @SuppressLint("InflateParams")
  private final View window;
  private final MainActivity context;

  @SuppressLint("InflateParams")
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

  /**
   * Show window if clicked or hide if clicked twice.
   *
   * @param m marked clicked.
   * @return true.
   */
  public boolean markerClickListener(Marker m) {
    if (m.isInfoWindowShown()) {
      m.hideInfoWindow();
    } else {
      m.showInfoWindow();
    }
    Log.d(TAG, "Mark clicked");
    return true;
  }

  /**
   * Navigates to post clicked.
   *
   * @param marker marker clicked.
   */
  public void infoWindowClickListener(Marker marker) {
    Log.d(TAG, "InfoWindow clicked");

    Trip trip = (Trip) marker.getTag();
    if (trip != null) {
      context.getNavigationGraph().navigateToPostPageExternal(trip);
    }
  }
}
