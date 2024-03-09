package ru.hse.goodtrip.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository.
 */
@Data
@AllArgsConstructor
public class User {

  private final String userId;
  private final String displayName;
  private final String mainPhotoUrl;
}