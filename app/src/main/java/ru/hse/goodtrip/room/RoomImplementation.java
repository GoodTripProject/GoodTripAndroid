package ru.hse.goodtrip.room;

import android.app.Application;
import androidx.room.Room;
import java.util.List;
import ru.hse.goodtrip.room.entities.UserEntity;

/**
 * RoomImplementation that provides access to LocalStorage and AppPreferences.
 */
public class RoomImplementation extends Application {

  private static final String DATABASE_NAME = "GTLocalStorage";
  private static final int USER_KEY = 0;
  private static RoomImplementation instance;
  private LocalStorage localStorage;

  public static RoomImplementation getInstance() {
    return instance;
  }

  public UserEntity getLoggedUser() {
    List<UserEntity> users = localStorage.userDao().getUser();
    if (!users.isEmpty()) {
      return users.get(0);
    }
    return null;
  }

  public void setLoggedUser(String name, String password) {
    if (getLoggedUser() == null) {
      localStorage.userDao().insert(
          new UserEntity(USER_KEY, name, password));
    } else {
      localStorage.userDao().update(
          new UserEntity(USER_KEY, name, password));
    }
    AppPreferences.setUserLoggedIn(this, true);
  }

  public void logOutUser() {
    UserEntity user = getLoggedUser();
    if (user != null) {
      localStorage.userDao().deleteById(user.uid);
    }
    AppPreferences.setUserLoggedIn(this, false);
  }

  public boolean isUserLoggedIn() {
    return AppPreferences.isUserLoggedIn(this);
  }

  @Override
  public void onCreate() {
    super.onCreate();

    instance = this;
    localStorage = Room.databaseBuilder(getApplicationContext(), LocalStorage.class, DATABASE_NAME)
        .allowMainThreadQueries()
        .build();
  }

  public LocalStorage getLocalStorage() {
    return localStorage;
  }
}