package ru.hse.goodtrip.ui.authentication;

import androidx.annotation.Nullable;
import lombok.Getter;

/**
 * Data validation state of the sign up form.
 */
@Getter
public class SignUpFormState {

  @Nullable
  private final Integer usernameError;
  @Nullable
  private final Integer passwordError;
  @Nullable
  private final Integer repeatedPasswordError;
  @Nullable
  private final Integer handlerError;

  private final boolean isDataValid;

  SignUpFormState(@Nullable Integer usernameError, @Nullable Integer passwordError,
      @Nullable Integer handlerError, @Nullable Integer repeatedPasswordError) {
    this.usernameError = usernameError;
    this.passwordError = passwordError;
    this.handlerError = handlerError;
    this.repeatedPasswordError = repeatedPasswordError;
    this.isDataValid = false;
  }

  SignUpFormState(boolean isDataValid) {
    this.usernameError = null;
    this.passwordError = null;
    this.handlerError = null;
    this.repeatedPasswordError = null;
    this.isDataValid = isDataValid;
  }
}
