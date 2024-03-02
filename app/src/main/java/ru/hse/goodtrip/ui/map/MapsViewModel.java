package ru.hse.goodtrip.ui.map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class MapsViewModel {

  private final MutableLiveData<String> text;

  public MapsViewModel() {
    text = new MutableLiveData<>();
    text.setValue("There will be a map!");
  }

  public LiveData<String> getText() {
    return text;
  }

}
