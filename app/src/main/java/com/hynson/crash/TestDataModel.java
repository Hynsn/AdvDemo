package com.hynson.crash;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class TestDataModel {

    private static volatile TestDataModel sInstance = new TestDataModel();

    public List<Activity> activities = new ArrayList<>();

    public static TestDataModel getInstance(){
        return sInstance;
    }
}
