package ru.hse.goodtrip.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import ru.hse.goodtrip.room.dao.UserDao;
import ru.hse.goodtrip.room.entities.UserEntity;

/**
 * Local Storage to store info about logged user.
 */
@Database(entities = {UserEntity.class}, version = 2)
public abstract class LocalStorage extends RoomDatabase {

  public abstract UserDao userDao();
}
