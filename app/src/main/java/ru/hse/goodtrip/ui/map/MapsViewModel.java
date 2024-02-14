package ru.hse.goodtrip.ui.map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class MapsViewModel {
    private final MutableLiveData<String> mText;

    public MapsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("There will be a map!");
    }

    public LiveData<String> getText() {
        return mText;
    }

}
