package ru.hse.goodtrip.ui.feed;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ru.hse.goodtrip.databinding.FeedLoadingViewBinding;
import ru.hse.goodtrip.databinding.PostTripBinding;

public class FeedViewHolders {

  public static class FeedLoadingViewHolder extends RecyclerView.ViewHolder {

    FeedLoadingViewBinding binding;

    public FeedLoadingViewHolder(@NonNull FeedLoadingViewBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }
  }

  public static class FeedPostViewHolder extends RecyclerView.ViewHolder {

    PostTripBinding binding;

    FeedPostViewHolder(PostTripBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }
  }
}
