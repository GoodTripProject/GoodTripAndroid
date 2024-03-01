package ru.hse.goodtrip.model;

import androidx.lifecycle.ViewModel;
import java.util.Collections;
import java.util.List;

public class TripsRepository {

  public List<ViewModel> listeners = Collections.emptyList();

  public void notifyNewPost() {

  }
}
