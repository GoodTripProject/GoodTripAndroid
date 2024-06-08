package ru.hse.goodtrip;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import ru.hse.goodtrip.room.LocalStorage;
import ru.hse.goodtrip.room.RoomImplementation;
import ru.hse.goodtrip.room.entities.UserEntity;

@RunWith(AndroidJUnit4.class)
public class RoomImplementationTest {

  private LocalStorage localStorage;
  private RoomImplementation roomImplementation;

  @Before
  public void createDb() {
    Context context = ApplicationProvider.getApplicationContext();
    localStorage = Room.inMemoryDatabaseBuilder(context, LocalStorage.class).build();
    roomImplementation = new RoomImplementation() {
      @Override
      public LocalStorage getLocalStorage() {
        return localStorage;
      }

      @Override
      public Context getContext() {
        return context;
      }
    };
  }

  @After
  public void closeDb() {
    localStorage.close();
  }

  @Test
  public void testSetLoggedUser() {
    String name = "testUser";
    String password = "testPassword";
    UserEntity testUser = TestUtil.createUser(name, password);

    roomImplementation.setLoggedUser(name, password);

    UserEntity user = roomImplementation.getLoggedUser();
    assertNotNull(user);
    assertEquals(user.name, testUser.name);
    assertEquals(user.password, testUser.password);

    assertTrue(roomImplementation.isUserLoggedIn());
  }

  @Test
  public void testLogOutUser() {
    String name = "testUser";
    String password = "testPassword";
    roomImplementation.setLoggedUser(name, password);
    roomImplementation.logOutUser();

    UserEntity user = roomImplementation.getLoggedUser();
    assertNull(user);
    assertFalse(roomImplementation.isUserLoggedIn());
  }

  @Test
  public void testIsUserLoggedIn() {
    String name = "testUser";
    String password = "testPassword";
    roomImplementation.setLoggedUser(name, password);

    assertTrue(roomImplementation.isUserLoggedIn());
    roomImplementation.logOutUser();
    assertFalse(roomImplementation.isUserLoggedIn());
  }
}
