package com.example.main;

import android.app.Application;
import android.content.Context;

import com.example.webview.HttpServer;

public class TestApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
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
        //CrashHandler.getInstance().init(base);
    }
}
