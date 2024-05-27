package ru.hse.goodtrip.network.firebase;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import com.google.common.hash.Hashing;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;
import ru.hse.goodtrip.data.UsersRepository;

public class FirebaseUtils {


  /**
   * Serialize image as a Bitmap.
   *
   * @param contentResolver Resolver of content.
   * @param localPhotoUrl   Url of photo at local storage.
   * @return Bitmap of photo.
   */
  static public Bitmap serializeImage(ContentResolver contentResolver, Uri localPhotoUrl) {
    try {
      return MediaStore.Images.Media.getBitmap(contentResolver,
          localPhotoUrl);
    } catch (IOException e) {
      Log.d(FirebaseUtils.class.getSimpleName(), Objects.requireNonNull(e.getLocalizedMessage()));
      return null;
    }
  }

  /**
   * Upload image to Firebase database.
   *
   * @param bitmap                    Bitmap of photo.
   * @param setImageUrlAfterUploading Consumer, which called in a callback.
   */
  static public void uploadImageToFirebase(Bitmap bitmap,
      Consumer<Uri> setImageUrlAfterUploading) {
    String filename = Hashing.sha256().hashString(
            UsersRepository.getInstance().user.getDisplayName()
                + UsersRepository.getInstance().user.getToken()
                + UsersRepository.getInstance().user.getHandle()
                + UsersRepository.getInstance().user.hashCode()
                + UUID.randomUUID()
                + Arrays.hashCode(bitmap.getNinePatchChunk()), StandardCharsets.UTF_8)
        .toString();
    StorageReference storageRef = FirebaseStorage.getInstance().getReference().child(
        filename);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
    Log.d(FirebaseUtils.class.getSimpleName(), filename);
    UploadTask uploadTask = storageRef.putBytes(baos.toByteArray());
    uploadTask.continueWithTask(task -> {
      if (!task.isSuccessful()) {
        throw Objects.requireNonNull(task.getException());
      }
      return storageRef.getDownloadUrl();
    }).addOnCompleteListener(task -> {
      if (task.isSuccessful()) {
        setImageUrlAfterUploading.accept(task.getResult());
      } else {
        Log.d(FirebaseUtils.class.getSimpleName(), "Task uploading is not successful");
      }
    });
  }
}
