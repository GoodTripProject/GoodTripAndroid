package ru.hse.goodtrip.room;

import android.app.Application;
import androidx.room.Room;
import ru.hse.goodtrip.data.model.User;
import ru.hse.goodtrip.room.entities.UserEntity;

public class RoomImplementation extends Application {

  private final static String DATABASE_NAME = "GtLocalStorage";
  private final static int USER_KEY = 0;
  private static RoomImplementation instance;
  private LocalStorage localStorage;

  public static RoomImplementation getInstance() {
    return instance;
  }

  public void setLoggedUser(User user) {
    localStorage.userDao().insert(
        new UserEntity(USER_KEY, user.getDisplayName(), user.getHandle(), user.getMainPhotoUrl()));
    AppPreferences.setUserLoggedIn(this, true);
  }

  public boolean isUserLoggedIn() {
    return AppPreferences.isUserLoggedIn(this);
  }

  @Override
  public void onCreate() {
    super.onCreate();

    instance = this;
    localStorage = Room.databaseBuilder(getApplicationContext(), LocalStorage.class, DATABASE_NAME)
        .build();
  }

  public LocalStorage getLocalStorage() {
    return localStorage;
  }
}