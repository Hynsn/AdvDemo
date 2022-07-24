package com.hynson.navigation;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NavigationVM extends ViewModel {
    private MutableLiveData<Integer> progress;

    public MutableLiveData<Integer> getProgress() {
        if(progress ==null) progress = new MutableLiveData<>();
        return progress;
    }
}
