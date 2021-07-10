package com.example;

import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;

import com.example.aidl.AidlActivity;
import com.example.coroutine.CoroutineActivity;
import com.example.crash.CrashActivity;
import com.example.customview.CustomViewActivity;
import com.example.databinding.DBLoginActivity;
import com.example.databinding.MainBinding;
import com.example.mbedtls.MbedtlsActivity;
import com.example.navigation.NavigationActivity;
import com.example.opensl.OpenslActivity;
import com.example.webview.WebviewActivity;

public class MainActivity extends FragmentActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    MainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.main);
        binding.setActivity(this);

        Message message = Message.obtain();
        Handler handler = new Handler(getMainLooper());
        Message msg = handler.obtainMessage();
        msg.arg1 = 2;
        handler.sendMessage(msg);

        Log.i(TAG, "onCreate: "+",hash: "+System.identityHashCode(this));
        Log.v("","");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        Log.i(TAG, "onRestoreInstanceState: "+",hash: "+System.identityHashCode(this));
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        Log.i(TAG, "onConfigurationChanged: "+",hash: "+System.identityHashCode(this));
    }

    public void test(View v) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        switch (v.getId()) {
            case R.id.btn_customview:
                intent.setComponent(new ComponentName(this, CustomViewActivity.class));
                break;
            case R.id.btn_webview:
                intent.setComponent(new ComponentName(this, WebviewActivity.class));
                break;
            case R.id.btn_databinding:
                intent.setComponent(new ComponentName(this, DBLoginActivity.class));
                break;
            case R.id.btn_navigation:
                intent.setComponent(new ComponentName(this, NavigationActivity.class));
                break;
            case R.id.btn_crashed:
                intent.setComponent(new ComponentName(this, CrashActivity.class));
                break;
            case R.id.btn_mbedtls:
                intent.setComponent(new ComponentName(this, MbedtlsActivity.class));
                break;
            case R.id.btn_opensl:
                intent.setComponent(new ComponentName(this, OpenslActivity.class));
                break;
            case R.id.btn_ipcaidl:
                intent.setComponent(new ComponentName(this, AidlActivity.class));
                break;
            case R.id.btn_ktcoroutine:
                intent.setComponent(new ComponentName(this, CoroutineActivity.class));
                break;
        }
        startActivity(intent);
    }
}
