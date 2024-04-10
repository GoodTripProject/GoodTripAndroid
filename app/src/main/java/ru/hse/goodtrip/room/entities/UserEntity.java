package ru.hse.goodtrip.room.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

  @PrimaryKey
  public int uid;

  @ColumnInfo(name = "name")
  public String name;

  @ColumnInfo(name = "password")
  public String password;
}
