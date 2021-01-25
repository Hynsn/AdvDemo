package com.example;

import android.app.Application;
import android.content.Context;

public class TestApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //CrashHandler.getInstance().init(base);
    }
}
