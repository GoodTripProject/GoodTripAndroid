package ru.hse.goodtrip.ui.feed;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FeedViewModel extends ViewModel {

  private final MutableLiveData<String> text;

  public FeedViewModel() {
    text = new MutableLiveData<>();
    text.setValue("There will be a feed!");
  }

  public LiveData<String> getText() {
    return text;
  }
}