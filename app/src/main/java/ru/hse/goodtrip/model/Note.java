package ru.hse.goodtrip.model;

import java.io.File;

public class Note {

  String headline;
  String note;
  File photo;
  AbstractPlace place;

  public Note(String headline, String note, File photo, AbstractPlace place) {
    this.headline = headline;
    this.note = note;
    this.photo = photo;
    this.place = place;
  }
}
