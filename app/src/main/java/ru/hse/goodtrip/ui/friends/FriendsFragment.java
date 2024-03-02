package ru.hse.goodtrip.ui.friends;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import ru.hse.goodtrip.R;

public class FriendsFragment extends Fragment {

  private FriendsViewModel friendsViewModel;

  public static FriendsFragment newInstance() {
    return new FriendsFragment();
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_friends, container, false);
  }

}