package com.hynson.crash;

import android.util.Log;
import android.view.View;

import com.fastdroid.base.BaseActivity;
import com.hynson.R;
import com.hynson.databinding.ActivityCrashBinding;

public class CrashActivity extends BaseActivity<ActivityCrashBinding> {
    @Override
    protected int getLayout() {
        return R.layout.activity_crash;
    }

    @Override
    protected void bindView() {

        Log.i("CrashActivity",""+Thread.getDefaultUncaughtExceptionHandler().toString());
//        TestDataModel.getInstance().activities.add(this);
    }

    public void click(View v){
        switch (v.getId()){
            case R.id.btn_maincrash:
                String s = null;
                if(s.length() > 0) {
                    throw new RuntimeException("主线程异常");
                }
                break;
            case R.id.btn_threadcrash:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String s = null;
                        if(s.length() > 0) {
                            //throw new RuntimeException("主线程异常");
                        }
                    }
                }).start();
                break;
            case R.id.btn_newcrash:
                String s2 = null;
                if(s2.length() > 0) {
                    throw new RuntimeException("主线程异常");
                }
                break;
        }
    }
}
