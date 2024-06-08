package ru.hse.goodtrip;

import java.util.Random;
import ru.hse.goodtrip.room.entities.UserEntity;

public class TestUtil {

  private static final Random random = new Random();

  public static UserEntity createUser(String name, String password) {
    return new UserEntity(random.nextInt(), name, password);
  }
}
