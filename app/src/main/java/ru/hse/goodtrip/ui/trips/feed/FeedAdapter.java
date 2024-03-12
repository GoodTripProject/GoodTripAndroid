package ru.hse.goodtrip.ui.trips.feed;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.bumptech.glide.Glide;
import java.util.Collections;
import java.util.List;
import ru.hse.goodtrip.MainActivity;
import ru.hse.goodtrip.R;
import ru.hse.goodtrip.data.model.trips.CountryVisit;
import ru.hse.goodtrip.data.model.trips.Trip;
import ru.hse.goodtrip.databinding.FeedLoadingViewBinding;
import ru.hse.goodtrip.databinding.ItemPostTripBinding;
import ru.hse.goodtrip.ui.trips.feed.FeedViewHolders.FeedLoadingViewHolder;
import ru.hse.goodtrip.ui.trips.feed.FeedViewHolders.FeedPostViewHolder;

public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
    implements View.OnClickListener {

  private static final int VIEW_TYPE_ITEM = 0;
  private static final int VIEW_TYPE_LOADING = 1;
  private static final String TAG = "FEED_ADAPTER";
  List<Trip> items = Collections.emptyList();

  @SuppressLint("NotifyDataSetChanged")
  public void setItems(List<Trip> newItems) {
    items = newItems;
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    if (viewType == VIEW_TYPE_ITEM) {
      ItemPostTripBinding binding = ItemPostTripBinding.inflate(
          LayoutInflater.from(parent.getContext()),
          parent, false);
      return new FeedPostViewHolder(binding);
    } else { // VIEW_TYPE_LOADING
      FeedLoadingViewBinding binding = FeedLoadingViewBinding.inflate(
          LayoutInflater.from(parent.getContext()), parent, false);
      return new FeedLoadingViewHolder(binding);
    }
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
    if (viewHolder instanceof FeedPostViewHolder) {
      showPostView((FeedPostViewHolder) viewHolder, position);
    } else if (viewHolder instanceof FeedLoadingViewHolder) {
      Log.d(TAG, "LoadingView appeared");
    }
  }

  private void showPostView(FeedPostViewHolder viewHolder, int position) {
    Trip trip = items.get(position);
    viewHolder.itemView.setOnClickListener(this);
    viewHolder.itemView.setTag(trip);
    ItemPostTripBinding binding = viewHolder.getBinding();
    setPostInfoWithTrip(trip, binding);
  }

  private void setPostInfoWithTrip(Trip trip, ItemPostTripBinding binding) {
    String dateFormat = "dd.MM.yyyy";
    StringBuilder countries = new StringBuilder();
    for (CountryVisit country : trip.getCountries()) {
      countries.append(country.getCountry().getName());
    }

    binding.titleText.setText(trip.getTitle());
    binding.profileNameText.setText(trip.getUser().getDisplayName());
    binding.tripDuration.setText(trip.getDuration(dateFormat));
    binding.dateOfPublication.setText(
        Trip.getDateFormatted(trip.getTimeOfPublication(), dateFormat));
    binding.countriesText.setText(countries);

    setImageByUrl(binding.profileImageView, trip.getUser().getMainPhotoUrl(),
        R.drawable.baseline_account_circle_24
    );
    setImageByUrl(binding.postImageView, trip.getMainPhotoUrl(), R.drawable.kazantip);
  }

  /**
   * Load image into imageView by URL
   *
   * @param imageView      where photo should be displayed
   * @param photoUrl       photo url
   * @param defaultImageId image that displays if error occurred (or photoUrl is null)
   */
  private void setImageByUrl(ImageView imageView, @Nullable String photoUrl, int defaultImageId) {
    if (photoUrl != null && !photoUrl.trim().isEmpty()) {
      Glide.with(imageView.getContext())
          .load(photoUrl)
          .error(defaultImageId)
          .into(imageView);
    } else {
      Glide.with(imageView.getContext()).clear(imageView);
      imageView.setImageResource(defaultImageId);
    }
  }

  public void showLoadingView() {
    items.add(0, null);
    notifyItemInserted(0);
  }

  public void hideLoadingView() {
    items.remove(null);
    notifyItemRemoved(0);
  }

  @Override
  public int getItemCount() {
    return items.size();
  }

  @Override
  public void onClick(View v) {
    Trip postClicked = (Trip) v.getTag();

    MainActivity activity = (MainActivity) v.getContext();
    activity.getNavigationGraph().navigateToPostPage(postClicked);
  }

  public void onPostInfo(Trip postTrip) {
    // TODO: action with clicked post
  }

  @Override
  public int getItemViewType(int position) {
    return items.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
  }
}
