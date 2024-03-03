package ru.hse.goodtrip.ui.profile;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.github.dhaval2404.imagepicker.ImagePicker;
import ru.hse.goodtrip.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {

  private ProfileViewModel profileViewModel;
  private FragmentProfileBinding binding;

  //TODO нужно будет сделать, чтобы при создании окна передавалось фото из бд
  private Uri uri;

  private final ActivityResultLauncher<Intent> pickMedia = registerForActivityResult(
      new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
          Intent data = result.getData();
          if (data != null && data.getData() != null) {
            setUri(data.getData());
            binding.profileImage.setImageURI(uri);
            //TODO сохранять в бд как-то
          } else {
            //TODO
          }
        }

      });

  public static ProfileFragment newInstance() {
    return new ProfileFragment();
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater,
      ViewGroup container, Bundle savedInstanceState) {
    profileViewModel =
        new ViewModelProvider(this).get(ProfileViewModel.class);
    binding = FragmentProfileBinding.inflate(inflater, container, false);
    View root = binding.getRoot();
    binding.profileImage.setOnClickListener(v -> {
      ImagePicker.with(this)
          .cropSquare()
          .compress(180)
          .maxResultSize(180, 180)
          .createIntent(intent -> {
            pickMedia.launch(intent);
            return null;
          });

    });

    if (uri != null) {
      binding.profileImage.setImageURI(uri);
    }

    return root;
  }

  public void setUserInfo(String fullName, String handle) {
    binding.fullnameView.setText(fullName);
    binding.handleView.setText("@".concat(handle));
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