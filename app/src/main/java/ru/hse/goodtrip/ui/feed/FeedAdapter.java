package ru.hse.goodtrip.ui.feed;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.bumptech.glide.Glide;
import java.util.Collections;
import java.util.List;
import ru.hse.goodtrip.R;
import ru.hse.goodtrip.databinding.FeedLoadingViewBinding;
import ru.hse.goodtrip.databinding.PostTripBinding;
import ru.hse.goodtrip.ui.feed.FeedViewHolders.FeedLoadingViewHolder;
import ru.hse.goodtrip.ui.feed.FeedViewHolders.FeedPostViewHolder;

public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
    implements View.OnClickListener {

  private static final int VIEW_TYPE_ITEM = 0;
  private static final int VIEW_TYPE_LOADING = 1;
  List<PostTrip> items = Collections.emptyList();

  @SuppressLint("NotifyDataSetChanged")
  public void setItems(List<PostTrip> newItems) {
    items = newItems;
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    if (viewType == VIEW_TYPE_ITEM) {
      PostTripBinding binding = PostTripBinding.inflate(LayoutInflater.from(parent.getContext()),
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
      String TAG = "FeedAdapter";
      Log.d(TAG, "LoadingView appeared");
    }
  }

  private void showPostView(FeedPostViewHolder viewHolder, int position) {
    PostTrip trip = items.get(position);
    PostTripBinding binding = viewHolder.getBinding();
    viewHolder.itemView.setOnClickListener(this);
    viewHolder.itemView.setTag(trip);
    binding.profileNameText.setText(trip.user.name);
    binding.titleText.setText(trip.title);
    binding.dateText.setText(trip.dateArrival);

    if (trip.user.photo != null && !trip.user.photo.isEmpty()) { // TODO: isBlank instead
      Glide.with(binding.profileImageView.getContext())
          .load(trip.user.photo)
          .circleCrop()
          .placeholder(R.drawable.baseline_account_circle_24) // TODO: replace with real photo
          .error(R.drawable.baseline_account_circle_24)
          .into(binding.profileImageView);
    } else {
      Glide.with(binding.profileImageView.getContext()).clear(binding.profileImageView);
      binding.profileImageView.setImageResource(R.drawable.baseline_account_circle_24);
    }

    if (trip.photo != null && !trip.photo.isEmpty()) { // TODO: isBlank instead
      Glide.with(binding.postImageView.getContext())
          .load(trip.photo)
          .placeholder(R.drawable.kazantip) // TODO: replace with real photo
          .error(R.drawable.kazantip)
          .into(binding.postImageView);
    } else {
      Glide.with(binding.postImageView.getContext()).clear(binding.postImageView);
      binding.postImageView.setImageResource(R.drawable.kazantip);
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
    PostTrip postClicked = (PostTrip) v.getTag();
    onPostInfo(postClicked);
  }

  public void onPostInfo(PostTrip postTrip) {
    // TODO: action with clicked post
  }

  @Override
  public int getItemViewType(int position) {
    return items.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
  }
}
