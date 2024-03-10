package ru.hse.goodtrip.ui.authentication;

import androidx.annotation.Nullable;
import lombok.Getter;
import ru.hse.goodtrip.data.model.User;

/**
 * Authentication result : success (user details) or error message.
 */
@Getter
class LoginResult {

  @Nullable
  private User success;
  @Nullable
  private Integer error;

  LoginResult(@Nullable Integer error) {
    this.error = error;
  }

  LoginResult(@Nullable User success) {
    this.success = success;
  }
}