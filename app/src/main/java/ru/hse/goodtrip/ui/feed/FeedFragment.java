package ru.hse.goodtrip.ui.feed;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.hse.goodtrip.MainActivity;
import ru.hse.goodtrip.databinding.FragmentFeedBinding;

public class FeedFragment extends Fragment {

  private FeedViewModel feedViewModel;
  private FragmentFeedBinding binding;

  private FeedRecyclerViewHolder feedRecyclerViewHolder;

  @Override
  public void onResume() {
    super.onResume();
    ((MainActivity) getActivity()).getSupportActionBar().hide();
  }

  @Override
  public void onStop() {
    super.onStop();
    ((MainActivity) getActivity()).getSupportActionBar().show();
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    feedViewModel =
        new ViewModelProvider(this).get(FeedViewModel.class);
    binding = FragmentFeedBinding.inflate(inflater, container, false);
    feedRecyclerViewHolder = new FeedRecyclerViewHolder(binding.recyclerView);
    return binding.getRoot();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }

  private class FeedRecyclerViewHolder {

    private final RecyclerView feedRecyclerView;
    private FeedAdapter feedAdapter;
    private LinearLayoutManager feedLayoutManager;
    private boolean isLoading = false;

    public FeedRecyclerViewHolder(RecyclerView recyclerView) {
      feedRecyclerView = recyclerView;

      initializeLayoutManager();
      initializeAdapter();
      initializeScrollListener();
    }


    /**
     * Initialize vertical LayoutManager for RecyclerView.
     */
    private void initializeLayoutManager() {
      feedLayoutManager = new LinearLayoutManager(getActivity());
      feedRecyclerView.setLayoutManager(feedLayoutManager);
    }

    /**
     * Initialize data adapter for RecyclerView.
     */
    private void initializeAdapter() {
      feedAdapter = new FeedAdapter();
      feedAdapter.setItems(feedViewModel.getPosts());
      feedRecyclerView.setAdapter(feedAdapter);
    }


    /**
     * Initialize feed refreshing when scrolled to bottom of list.
     */
    private void initializeScrollListener() {
      feedRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
          super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
          super.onScrolled(recyclerView, dx, dy);
          if (!isLoading && dy < 0) {
            if (feedLayoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
              isLoading = true;
              refreshFeed();
            }
          }
        }
      });
    }

    /**
     * Refresh feed with FeedViewModel.
     */
    private void refreshFeed() {
      feedRecyclerView.post(() -> {
            feedAdapter.showLoadingView();
            feedRecyclerView.postDelayed(() -> {
              loadData();

              feedAdapter.hideLoadingView();
              isLoading = false;
            }, 2000);
          }
      );
    }

    /**
     * Update data with TripsRepository.
     */
    public void loadData() {
      // TODO: Repository access in future
      feedAdapter.setItems(feedViewModel.getPosts());
    }
  }
}
