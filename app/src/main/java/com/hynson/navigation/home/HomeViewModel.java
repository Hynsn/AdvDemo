package com.hynson.navigation.home;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.base.base.FviewModel;

public class HomeViewModel extends FviewModel {

    private MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    @Override
    protected void registLifeOwner(LifecycleOwner owner) {

    }
}