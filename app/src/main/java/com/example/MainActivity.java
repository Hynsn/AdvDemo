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

public class MainActivity extends FragmentActivity {
    MainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.main);
        binding.setActivity(this);
    }

    public void test(View v){
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        switch (v.getId()){
            case R.id.btn_databinding:
                intent.setComponent(new ComponentName(this, DBLoginActivity.class));
                startActivity(intent);
                break;
            case R.id.btn_timeline:
                intent.setComponent(new ComponentName(this, TimeLineActivity.class));
                startActivity(intent);
                break;
            case R.id.btn_crashed:
                intent.setComponent(new ComponentName(this, CrashActivity.class));
                startActivity(intent);
                break;
        }
    }
}
