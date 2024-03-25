package ru.hse.goodtrip.data.model;

import java.net.URL;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository.
 */
@Data
@AllArgsConstructor
public class User {

  private final String displayName;
  private final URL mainPhotoUrl;
  public String handle;
  public String name;
  public String surname;
  public String token;
  public URL url;
}