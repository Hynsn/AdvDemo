package com.hynson.main;

import android.app.Application;
import android.content.Context;

import com.hynson.crash.CrashHandler;
import com.hynson.webview.HttpServer;

public class TestApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.getInstance().init(this,Thread.getDefaultUncaughtExceptionHandler());
            new Thread(new Runnable() {
                @Override
                public void run() {
                try {
                    HttpServer server = new HttpServer(8080);
                    server.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }}).start();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        CrashHandler.getInstance().init(base);
    }
}
