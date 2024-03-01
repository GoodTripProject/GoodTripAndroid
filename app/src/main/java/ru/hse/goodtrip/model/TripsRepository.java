package ru.hse.goodtrip.model;

import androidx.lifecycle.ViewModel;
import java.util.Collections;
import java.util.List;

/**
 * Repository that provides ViewModels(Feed, Map, Profile) actual data about trips.
 */
public class TripsRepository {

  public List<ViewModel> listeners = Collections.emptyList();

  public void notifyNewPost() {

  }
}
