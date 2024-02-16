package ru.hse.goodtrip.ui.profile;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import ru.hse.goodtrip.databinding.FragmentProfileBinding;
import ru.hse.goodtrip.ui.places.PlacesViewModel;

public class ProfileFragment extends Fragment {

  Button backButton;
  private ProfileViewModel profileViewModel;
  private FragmentProfileBinding binding;
  private Uri uri;
  private final ActivityResultLauncher<PickVisualMediaRequest> pickMedia = registerForActivityResult(
      new ActivityResultContracts.PickVisualMedia(), uri ->
      {
        if (uri != null) {
          setUri(uri);
          binding.profileImage.setImageURI(uri);
          //TODO сохранять в бд как-то
        } else {
          //TODO
        }

      });

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater,
      ViewGroup container, Bundle savedInstanceState) {
    PlacesViewModel placesViewModel =
        new ViewModelProvider(this).get(PlacesViewModel.class);

    binding = FragmentProfileBinding.inflate(inflater, container, false);
    View root = binding.getRoot();

    binding.goBackButton.setOnClickListener(this);
    binding.profileImage.setOnClickListener(
        v -> pickMedia.launch(new PickVisualMediaRequest.Builder()
            .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
            .build()));

    return root;
  }

  public void goBack() {
    getParentFragmentManager().popBackStack();
  }

  public void setUri(Uri uri) {
    this.uri = uri;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }

}