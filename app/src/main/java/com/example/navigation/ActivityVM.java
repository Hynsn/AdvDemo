package com.example.navigation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ActivityVM extends AndroidViewModel {
    private MutableLiveData<String> name;

    public MutableLiveData<String> getName() {
        if(name==null){
            name = new MutableLiveData<>();
        }
        return name;
    }

    public ActivityVM(@NonNull Application application) {
        super(application);
    }
}
