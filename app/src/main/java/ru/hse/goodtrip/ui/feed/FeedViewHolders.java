package ru.hse.goodtrip.ui.feed;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ru.hse.goodtrip.databinding.FeedLoadingViewBinding;
import ru.hse.goodtrip.databinding.ItemPostTripBinding;

/**
 * Main FeedViewHolders class used as namespace for ViewHolders definition.
 */
public class FeedViewHolders {

  /**
   * Creates LoadingViewHolder to appearing when new data is uploading in FeedAdapter.
   */
  public static class FeedLoadingViewHolder extends RecyclerView.ViewHolder {

    private FeedLoadingViewBinding binding;

    public FeedLoadingViewHolder(@NonNull FeedLoadingViewBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    public FeedLoadingViewBinding getBinding() {
      return binding;
    }

  }

  /**
   * Creates PostViewHolder to appearing Post in RecyclerView container.
   */
  public static class FeedPostViewHolder extends RecyclerView.ViewHolder {

    private ItemPostTripBinding binding;

    FeedPostViewHolder(ItemPostTripBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    public ItemPostTripBinding getBinding() {
      return binding;
    }
  }
}
