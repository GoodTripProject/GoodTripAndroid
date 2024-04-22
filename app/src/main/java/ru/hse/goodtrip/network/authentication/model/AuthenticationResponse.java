package ru.hse.goodtrip.network.authentication.model;

import java.io.Serializable;
import java.net.URL;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class AuthenticationResponse implements Serializable {

  public int id;
  public String handle;
  public String name;
  public String surname;
  public String token;
  public URL url;
}