package ru.hse.goodtrip.ui.trips.feed;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import lombok.Getter;
import ru.hse.goodtrip.databinding.FeedLoadingViewBinding;
import ru.hse.goodtrip.databinding.ItemPostTripBinding;

/**
 * Main FeedViewHolders class used as namespace for ViewHolders definition.
 */
public class FeedViewHolders {

  /**
   * Creates LoadingViewHolder to appearing when new data is uploading in FeedAdapter.
   */
  @Getter
  public static class FeedLoadingViewHolder extends RecyclerView.ViewHolder {

    private final FeedLoadingViewBinding binding;

    public FeedLoadingViewHolder(@NonNull FeedLoadingViewBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }
  }

  /**
   * Creates PostViewHolder to appearing Post in RecyclerView container.
   */
  @Getter
  public static class FeedPostViewHolder extends RecyclerView.ViewHolder {

    private final ItemPostTripBinding binding;

    FeedPostViewHolder(ItemPostTripBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }
  }
}
