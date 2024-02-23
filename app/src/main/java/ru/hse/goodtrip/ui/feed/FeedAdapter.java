package ru.hse.goodtrip.ui.feed;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Collections;
import java.util.List;
import ru.hse.goodtrip.databinding.PostTripBinding;
import ru.hse.goodtrip.model.Trip;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedHolder> {

  List<Trip> trips = Collections.emptyList();

  public void setTrips(List<Trip> newTrips) {
    trips = newTrips;
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public FeedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    PostTripBinding binding = PostTripBinding.inflate(LayoutInflater.from(parent.getContext()),
        parent, false);
    return new FeedHolder(binding);
  }

  @Override
  public void onBindViewHolder(@NonNull FeedHolder holder, int position) {
    Trip trip = trips.get(position);
//        var binding
  }

  @Override
  public int getItemCount() {
    return trips.size();
  }

  public class FeedHolder extends RecyclerView.ViewHolder {

    FeedHolder(PostTripBinding binding) {
      super(binding.getRoot());
    }
  }
}
