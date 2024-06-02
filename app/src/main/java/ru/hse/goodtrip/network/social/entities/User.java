package ru.hse.goodtrip.network.social.entities;

import lombok.Data;

@Data
public class User {

  private Integer id;

  private String username;

  private String handle;

  private String imageLink;

  private String name;

  private String surname;


}
