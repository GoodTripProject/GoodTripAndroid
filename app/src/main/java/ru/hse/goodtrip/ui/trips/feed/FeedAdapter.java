package ru.hse.goodtrip.ui.trips.feed;

import static ru.hse.goodtrip.ui.trips.feed.utils.Utils.getDateFormatted;
import static ru.hse.goodtrip.ui.trips.feed.utils.Utils.setImageByUrl;
import static ru.hse.goodtrip.ui.trips.feed.utils.Utils.setImageByUrlCropped;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import ru.hse.goodtrip.MainActivity;
import ru.hse.goodtrip.R;
import ru.hse.goodtrip.data.TripRepository;
import ru.hse.goodtrip.data.UsersRepository;
import ru.hse.goodtrip.data.model.Result;
import ru.hse.goodtrip.databinding.FeedLoadingViewBinding;
import ru.hse.goodtrip.databinding.ItemPostTripBinding;
import ru.hse.goodtrip.network.trips.model.CountryVisit;
import ru.hse.goodtrip.network.trips.model.TripView;
import ru.hse.goodtrip.ui.trips.feed.FeedViewHolders.FeedLoadingViewHolder;
import ru.hse.goodtrip.ui.trips.feed.FeedViewHolders.FeedPostViewHolder;

public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements
    View.OnClickListener {

  public static final String POST_ARG = "post";
  private static final int VIEW_TYPE_ITEM = 0;
  private static final int VIEW_TYPE_LOADING = 1;
  private static final String TAG = "FEED_ADAPTER";
  List<TripView> items = Collections.emptyList();

  @SuppressLint("NotifyDataSetChanged")
  public void setItems(List<TripView> newItems) {
    items = newItems;
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    if (viewType == VIEW_TYPE_ITEM) {
      ItemPostTripBinding binding = ItemPostTripBinding.inflate(
          LayoutInflater.from(parent.getContext()), parent, false);
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
    TripView trip = items.get(position);
    viewHolder.itemView.setOnClickListener(this);
    viewHolder.itemView.setTag(trip);
    ItemPostTripBinding binding = viewHolder.getBinding();
    setPostInfoWithTrip(trip, binding);

  }

  private void setPostInfoWithTrip(TripView trip, ItemPostTripBinding binding) {
    String dateFormat = "dd.MM.yyyy";
    StringBuilder countries = new StringBuilder();
    for (CountryVisit country : trip.getVisits()) {
      countries.append(country.getCountry());
    }
    binding.titleText.setText(trip.getTitle());
    binding.profileNameText.setText(trip.getDisplayName());
    binding.dateOfPublication.setText(getDateFormatted(trip.getPublicationTimestamp().toInstant()
        .atZone(ZoneId.systemDefault()).toLocalDate(), dateFormat));
    binding.countriesText.setText(countries);
    if (trip.getUserMainPhotoUrl() != null) {
      setImageByUrl(binding.profileImageView, trip.getUserMainPhotoUrl(),
          R.drawable.baseline_account_circle_24);
    } else {
      setImageByUrlCropped(binding.profileImageView,
          "https://hosting.photobucket.com/albums/ii87/aikhrabrov/Paris%20la%20nuit/img_6910.jpg",
          R.drawable.baseline_account_circle_24);
    }

    setImageByUrl(binding.postImageView, trip.getMainPhotoUrl(), R.drawable.kazantip);
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
    TripView postClicked = (TripView) v.getTag();

    MainActivity activity = (MainActivity) v.getContext();
    Handler handler = new Handler(activity.getMainLooper());
    TripRepository.getInstance()
        .getTripById(postClicked.getId(), UsersRepository.getInstance().user.getToken())
        .thenAccept((fullTrip) -> {
          if (fullTrip.isSuccess()) {
            Log.d(TAG, "Get trip by id is happened, id of trip is:" + postClicked.getId());

          } else {
            Log.e(TAG, "Get trip by id is happened, issues happened, id of trip is:"
                + postClicked.getId());

          }
          handler.post(() -> {
            ObjectMapper objectMapper = new ObjectMapper();
            activity.getNavigationGraph().navigateToPostPage(TripRepository.getTripFromTripResponse(
                objectMapper.convertValue(((Result.Success<?>) fullTrip).getData(),
                    ru.hse.goodtrip.network.trips.model.Trip.class)));
          });
        });
  }

  @Override
  public int getItemViewType(int position) {
    return items.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
  }
}
