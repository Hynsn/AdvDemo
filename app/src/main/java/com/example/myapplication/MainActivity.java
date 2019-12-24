package com.example.myapplication;

import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;

import com.example.myapplication.databinding.TestMainBinding;

public class MainActivity extends FragmentActivity {
    final static String TAG = "Main";

    private UserInfo user;
    private TestMainBinding binding;
    private Time mTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.test_main);
        mTime = new Time();
        binding = DataBindingUtil.setContentView(MainActivity.this, R.layout.test_main);
        user = new UserInfo("","");
        binding.setUser(user);
        binding.setActivity(this);
    }

    public void userLogin(View v){
        mTime.setToNow();
        int year = mTime.year;
        int month = mTime.month+1;
        int day = mTime.monthDay;
        int hour = mTime.hour;
        int minute = mTime.minute;
        // 双向绑定
        Log.d(TAG, "userLogin: "+user.getName()+","+user.getPwd());
        Toast.makeText(getApplicationContext(),user.getName()+"登录中",Toast.LENGTH_SHORT).show();
        // 单向绑定
        user.setLoginTime("上次登录时间:"+year+"年"+month+"月"+day+"日"+hour+":"+minute);
    }
}
