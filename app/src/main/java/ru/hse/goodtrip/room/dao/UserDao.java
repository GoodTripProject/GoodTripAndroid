package ru.hse.goodtrip.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;
import ru.hse.goodtrip.room.entities.UserEntity;

@Dao
public interface UserDao {

  @Query("SELECT * FROM userEntity")
  List<UserEntity> getUser();

  @Insert
  void insert(UserEntity user);

  @Delete
  void delete(UserEntity user);
}
