package com.example.databinding;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.R;
import com.example.base.BaseActivity;
import com.example.databinding.entity.UserInfo;

public class DBLoginActivity extends BaseActivity<ActivityDataBindingLoginBinding> {
    final static String TAG = DBLoginActivity.class.getSimpleName();

    private UserInfo user;
    private Time mTime;

    @Override
    protected int getLayout() {
        return R.layout.activity_data_binding_login;
    }

    @Override
    protected void bindView() {

        mTime = new Time();
        user = new UserInfo("","");
        binding.setUser(user);
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

        checkUser(user,this);
        // 单向绑定
        user.setLoginTime("上次登录时间:"+year+"年"+month+"月"+day+"日"+hour+":"+minute);
    }

    private void checkUser(final UserInfo user, final Context context){
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(2000);
                if(user.getName().equals("test") && user.getPwd().equals("1234")){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent();
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setComponent(new ComponentName(context, DBNoteActivity.class));
                            startActivity(intent);
                        }
                    });
                }
                else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"验证错误",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }
}
