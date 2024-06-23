package ru.hse.goodtrip.ui.profile;

import static ru.hse.goodtrip.ui.trips.feed.utils.Utils.setImageByUrl;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import ru.hse.goodtrip.MainActivity;
import ru.hse.goodtrip.data.UsersRepository;
import ru.hse.goodtrip.data.model.User;
import ru.hse.goodtrip.databinding.FragmentProfileBinding;
import ru.hse.goodtrip.network.firebase.FirebaseUtils;
import ru.hse.goodtrip.room.RoomImplementation;
import ru.hse.goodtrip.ui.profile.followers.FollowingFragment.PAGE_TYPE;

/**
 * ProfileFragment screen.
 */
public class ProfileFragment extends Fragment {

  UsersRepository repository = UsersRepository.getInstance();
  @SuppressWarnings("FieldCanBeLocal")
  private ProfileViewModel profileViewModel;
  private FragmentProfileBinding binding;
  private User user;
  /**
   * Launch app intent to upload image from album or from gallery. Then compress and serialize image
   * and upload it to Firebase. After this, set up new image to profile.
   */
  private final ActivityResultLauncher<Intent> pickMedia = registerForActivityResult(
      new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
          Intent data = result.getData();
          if (data != null && data.getData() != null) {
            Bitmap bitmap = FirebaseUtils.serializeImage(requireContext().getContentResolver(),
                Uri.parse(data.getData().toString()));
            if (bitmap != null) {
              FirebaseUtils.uploadImageToFirebase(
                  bitmap,
                  (uri) -> {
                    repository.updatePhoto(user.getId(), uri.toString(),
                        UsersRepository.getInstance().user.getToken());
                    try {
                      user.setMainPhotoUrl(new URL(uri.toString()));
                    } catch (MalformedURLException e) {
                      Log.d(this.getClass().getSimpleName(),
                          Objects.requireNonNull(e.getLocalizedMessage()));
                    }
                  }
              );
            }
            setImageByUrl(binding.profileImage, data.getData().toString());
          }
        }
      });

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater,
      ViewGroup container, Bundle savedInstanceState) {
    profileViewModel =
        new ViewModelProvider(this).get(ProfileViewModel.class);
    binding = FragmentProfileBinding.inflate(inflater, container, false);
    user = profileViewModel.getUser();
    return binding.getRoot();
  }

  /**
   * Starts intent that upload image by Camera or opens Gallery. Gets URI of image, then upload it
   * to Firebase.
   */
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
    setButtonClickListeners();
  }

  private void setButtonClickListeners() {
    binding.showFollowing.setOnClickListener(
        v -> ((MainActivity) requireActivity()).getNavigationGraph()
            .navigateToFollowing(UsersRepository.getInstance().getLoggedUser(),
                UsersRepository.getInstance().getFollowing(),
                PAGE_TYPE.FOLLOWING));

    binding.showFollowers.setOnClickListener(
        v -> ((MainActivity) requireActivity()).getNavigationGraph()
            .navigateToFollowing(UsersRepository.getInstance().getLoggedUser(),
                UsersRepository.getInstance().getFollowers(),
                PAGE_TYPE.FOLLOWERS));
    binding.profileImage.setOnClickListener(v -> uploadImageFromGallery());
    binding.myTripsButton.setOnClickListener(v ->
        ((MainActivity) requireActivity()).getNavigationGraph().navigateToMyTrips());
    binding.logoutButton.setOnClickListener(v -> logoutFromAccount());
  }

  private void logoutFromAccount() {
    new AlertDialog.Builder(getContext())
        .setTitle("Log out")
        .setMessage("Do you really want to log out?")
        .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {
          if (RoomImplementation.getInstance().isUserLoggedIn()) {
            RoomImplementation.getInstance().logOutUser();
            UsersRepository.getInstance().logout();
          }
          ((MainActivity) requireActivity()).getNavigationGraph().navigateToLogin();
        })
        .setNegativeButton(android.R.string.no, null).show();
  }

  @SuppressLint("SetTextI18n")
  private void setUserInfo() {
    setImageByUrl(binding.profileImage, user.getMainPhotoUrl().toString());
    binding.fullnameView.setText(user.getDisplayName());
    binding.handleView.setText("@".concat(user.getHandle()));

    binding.followersCount.setText(
        Integer.toString(UsersRepository.getInstance().getFollowers().size() - 1));
    binding.followingCount.setText(
        Integer.toString(UsersRepository.getInstance().getFollowing().size() - 1));

  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }

}