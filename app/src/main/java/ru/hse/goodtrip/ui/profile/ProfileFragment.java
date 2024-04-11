package ru.hse.goodtrip.ui.profile;

import static ru.hse.goodtrip.ui.trips.feed.utils.Utils.setImageByUrl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.github.dhaval2404.imagepicker.ImagePicker;
import ru.hse.goodtrip.MainActivity;
import ru.hse.goodtrip.data.UsersRepository;
import ru.hse.goodtrip.data.model.User;
import ru.hse.goodtrip.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {

  private ProfileViewModel profileViewModel;
  private FragmentProfileBinding binding;
  private final ActivityResultLauncher<Intent> pickMedia = registerForActivityResult(
      new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
          Intent data = result.getData();
          if (data != null && data.getData() != null) {
            setImageByUrl(binding.profileImage, data.getData().toString());
            UsersRepository.changeUserMainPhoto(data.getData());
          }
        }
      });
  private User user;

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater,
      ViewGroup container, Bundle savedInstanceState) {
    profileViewModel =
        new ViewModelProvider(this).get(ProfileViewModel.class);
    binding = FragmentProfileBinding.inflate(inflater, container, false);
    user = profileViewModel.getUser();
    return binding.getRoot();
  }

  private void uploadImageFromGallery() {
    ImagePicker.with(this)
        .cropSquare()
        .compress(180)
        .maxResultSize(180, 180)
        .createIntent(intent -> {
          pickMedia.launch(intent);
          return null;
        });
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    setUserInfo();
    binding.profileImage.setOnClickListener(v -> uploadImageFromGallery());

    binding.myTripsButton.setOnClickListener(v ->
        ((MainActivity) requireActivity()).getNavigationGraph().navigateToMyTrips());
  }

  public void setUserInfo() {
    if (user.getMainPhotoUrl()!=null) {
      setImageByUrl(binding.profileImage, user.getMainPhotoUrl().toString());
    }
    binding.fullnameView.setText(user.getDisplayName());
    binding.handleView.setText("@".concat(user.getHandle()));
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }

}