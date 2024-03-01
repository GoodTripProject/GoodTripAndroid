package ru.hse.goodtrip.ui.feed;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import ru.hse.goodtrip.databinding.FragmentFeedBinding;

public class FeedFragment extends Fragment {

  private FeedViewModel mViewModel;
  private FragmentFeedBinding binding;

  public static FeedFragment newInstance() {
    return new FeedFragment();
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    FeedViewModel feedViewModel =
        new ViewModelProvider(this).get(FeedViewModel.class);

    binding = FragmentFeedBinding.inflate(inflater, container, false);
    View root = binding.getRoot();

//        final TextView textView = TextView;
//        feedViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
    return root;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }

}