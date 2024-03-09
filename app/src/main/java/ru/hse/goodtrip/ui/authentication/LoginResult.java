package ru.hse.goodtrip.ui.authentication;

import androidx.annotation.Nullable;
import lombok.Getter;

/**
 * Authentication result : success (user details) or error message.
 */
@Getter
class LoginResult {

  @Nullable
  private LoggedInUserView success;
  @Nullable
  private Integer error;

  LoginResult(@Nullable Integer error) {
    this.error = error;
  }

  LoginResult(@Nullable LoggedInUserView success) {
    this.success = success;
  }
}