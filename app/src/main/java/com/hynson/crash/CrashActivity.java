package com.hynson.crash;

import android.view.View;

import com.base.base.BaseActivity;
import com.hynson.R;
import com.hynson.databinding.ActivityCrashBinding;

public class CrashActivity extends BaseActivity<ActivityCrashBinding> {
    @Override
    protected int getLayout() {
        return R.layout.activity_crash;
    }

    @Override
    protected void bindView() {
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
        }
    }
}