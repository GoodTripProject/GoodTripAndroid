package ru.hse.goodtrip.room;

import android.app.Application;
import androidx.room.Room;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import ru.hse.goodtrip.room.entities.UserEntity;

public class RoomImplementation extends Application {

  private final static String DATABASE_NAME = "GTLocalStorage";
  private final static int USER_KEY = 0;
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
    ExecutorService executor = Executors.newSingleThreadExecutor();
    executor.execute(() -> {
      localStorage.userDao().insert(
          new UserEntity(USER_KEY, name, password));
      AppPreferences.setUserLoggedIn(this, true);
    });
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