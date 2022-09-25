package com.hynson.crash;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import static android.os.Looper.getMainLooper;

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static String TAG = CrashHandler.class.getSimpleName();

    private Context mContext;
    private Thread.UncaughtExceptionHandler orginal;
    private static CrashHandler mInstance;

    public void init(Context context) {
        this.mContext = context;
        orginal = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        Handler handler = new Handler(getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Looper.loop();
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public static CrashHandler getInstance() {
        if (mInstance == null) {
            synchronized (CrashHandler.class) {
                if (mInstance == null) {
                    mInstance = new CrashHandler();
                }
            }
        }
        return mInstance;
    }


    @Override
    public void uncaughtException(@NonNull Thread thread, @NonNull Throwable throwable) {
        Log.i(TAG, "uncaughtException: " + thread + "," + throwable.getMessage());
        throwable.printStackTrace();

        if (orginal != null) {
            orginal.uncaughtException(thread, throwable);
        }
    }
}
