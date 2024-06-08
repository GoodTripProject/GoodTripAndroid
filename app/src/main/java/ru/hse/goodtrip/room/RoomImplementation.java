package ru.hse.goodtrip.room;

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
  private static RoomImplementation instance;
  private LocalStorage localStorage;

  private Context context;

  public static RoomImplementation getInstance() {
    return instance;
  }

  public UserEntity getLoggedUser() {
    List<UserEntity> users = getLocalStorage().userDao().getUser();
    if (!users.isEmpty()) {
      return users.get(0);
    }
    return null;
  }

  public void setLoggedUser(String name, String password) {
    if (getLoggedUser() == null) {
      getLocalStorage().userDao().insert(
          new UserEntity(USER_KEY, name, password));
    } else {
      getLocalStorage().userDao().update(
          new UserEntity(USER_KEY, name, password));
    }
  }

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