package ru.hse.goodtrip.network.authentication.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Authorization request.
 */
@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class AuthorizationRequest implements Serializable {

  public String username;
  public String password;
}
