package ru.hse.goodtrip.model;

import java.util.ArrayList;

/**
 * Post data class.
 */
public class PostTrip {

  public String timeOfPublication;
  public String photo;
  public long id;
  public User user;
  public String title;
  public ArrayList<String> countries;
  public String dateArrival;
  public String dateDeparture;
  public Trip trip;
}
