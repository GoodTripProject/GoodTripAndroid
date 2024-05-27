package ru.hse.goodtrip.ui.map;

import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ru.hse.goodtrip.R;

public class MapsFollowingFragment extends Fragment {

  private MapsFollowingViewModel mViewModel;

  public static MapsFollowingFragment newInstance() {
    return new MapsFollowingFragment();
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_maps_following, container, false);
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    mViewModel = new ViewModelProvider(this).get(MapsFollowingViewModel.class);
    // TODO: Use the ViewModel
  }

}