package com.example;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;

import com.example.crash.CrashActivity;
import com.example.customview.TimeLineActivity;
import com.example.databinding.DBLoginActivity;
import com.example.databinding.MainBinding;
import com.example.mbedtls.MbedtlsActivity;
import com.example.navigation.NavigationActivity;
import com.example.webview.WebviewActivity;

public class MainActivity extends FragmentActivity {
    MainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.main);
        binding.setActivity(this);
    }

    public void test(View v) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        switch (v.getId()) {
            case R.id.btn_customview:
                intent.setComponent(new ComponentName(this, TimeLineActivity.class));
                startActivity(intent);
                break;
            case R.id.btn_webview:
                intent.setComponent(new ComponentName(this, WebviewActivity.class));
                startActivity(intent);
            break;
            case R.id.btn_databinding:
                intent.setComponent(new ComponentName(this, DBLoginActivity.class));
                startActivity(intent);
                break;
            case R.id.btn_navigation:
                intent.setComponent(new ComponentName(this, NavigationActivity.class));
                startActivity(intent);
                break;
            case R.id.btn_crashed:
                intent.setComponent(new ComponentName(this, CrashActivity.class));
                startActivity(intent);
                break;
            case R.id.btn_mbedtls:
                intent.setComponent(new ComponentName(this, MbedtlsActivity.class));
                startActivity(intent);
                break;
        }
    }
}
