package ru.hse.goodtrip.ui.places;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PlacesViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public PlacesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("There will be a places!");
    }

    public LiveData<String> getText() {
        return mText;
    }
}