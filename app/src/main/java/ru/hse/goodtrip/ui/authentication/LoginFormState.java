package ru.hse.goodtrip.ui.authentication;

import androidx.annotation.Nullable;
import lombok.Getter;

/**
 * Data validation state of the login form.
 */
@Getter
class LoginFormState {

  @Nullable
  private final Integer usernameError;
  @Nullable
  private final Integer passwordError;
  private final boolean isDataValid;

  LoginFormState(@Nullable Integer usernameError, @Nullable Integer passwordError) {
    this.usernameError = usernameError;
    this.passwordError = passwordError;
    this.isDataValid = false;
  }

  LoginFormState(boolean isDataValid) {
    this.usernameError = null;
    this.passwordError = null;
    this.isDataValid = isDataValid;
  }
}