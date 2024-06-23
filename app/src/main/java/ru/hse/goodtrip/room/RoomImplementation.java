package ru.hse.goodtrip.room;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import androidx.room.Room;
import java.util.List;
import ru.hse.goodtrip.room.entities.UserEntity;

/**
 * RoomImplementation that provides access to LocalStorage and AppPreferences.
 */
public class RoomImplementation extends Application {

  private static final String DATABASE_NAME = "GTLocalStorage";
  private static final int USER_KEY = 0;
  @SuppressLint("StaticFieldLeak")
  private static RoomImplementation instance;
  private LocalStorage localStorage;

  private Context context;

  public static RoomImplementation getInstance() {
    return instance;
  }

  /**
   * Get logged user from Room if exists.
   *
   * @return logged user.
   */
  public UserEntity getLoggedUser() {
    List<UserEntity> users = getLocalStorage().userDao().getUser();
    if (!users.isEmpty()) {
      return users.get(0);
    }
    return null;
  }

  /**
   * Save logged user in Room.
   *
   * @param name     name.
   * @param password hashed password.
   */
  public void setLoggedUser(String name, String password) {
    if (getLoggedUser() == null) {
      getLocalStorage().userDao().insert(
          new UserEntity(USER_KEY, name, password));
    } else {
      getLocalStorage().userDao().update(
          new UserEntity(USER_KEY, name, password));
    }
  }

  /**
   * Delete user from Room.
   */
  public void logOutUser() {
    UserEntity user = getLoggedUser();
    if (user != null) {
      getLocalStorage().userDao().deleteById(user.uid);
    }
  }

  public boolean isUserLoggedIn() {
    return getLoggedUser() != null;
  }

  @Override
  public void onCreate() {
    super.onCreate();

    instance = this;
    context = this;
    localStorage = Room.databaseBuilder(getApplicationContext(), LocalStorage.class, DATABASE_NAME)
        .allowMainThreadQueries()
        .build();
  }

  public LocalStorage getLocalStorage() {
    return localStorage;
  }

  public Context getContext() {
    return context;
  }
}