package ru.hse.goodtrip.network.authentication.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
public class RegisterRequest implements Serializable {
  public String username;
  public String handle;
  public String password;
  public String name;
  public String surname;
}
