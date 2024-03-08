package ru.hse.goodtrip.ui.login;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Class exposing authenticated user details to the UI.
 */
@Data
@AllArgsConstructor
class LoggedInUserView {

  private String displayName;
  //... other data fields that may be accessible to the UI
}