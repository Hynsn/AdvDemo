package com.example.main;

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

import com.example.R;
import com.example.aidl.AidlActivity;
import com.example.coroutine.CoroutineActivity;
import com.example.crash.CrashActivity;
import com.example.customview.CustomViewActivity;
import com.example.databinding.DBLoginActivity;
import com.example.databinding.MainBinding;
import com.example.floatkkey.FloatKeyActivity;
import com.example.mbedtls.MbedtlsActivity;
import com.example.navigation.NavigationActivity;
import com.example.opensl.OpenslActivity;
import com.example.topbar.TopBarActivity;
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
        Log.i("","");
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
            case R.id.btn_topbar:
                intent.setComponent(new ComponentName(this, TopBarActivity.class));
                break;
            case R.id.btn_float_key:
                intent.setComponent(new ComponentName(this, FloatKeyActivity.class));
                break;
        }
        startActivity(intent);
    }
//    public void showGuideView() {
//    View view = getWindow().getDecorView().findViewById(R.id.activity_main);
//    if (view == null) return;
//
//    ViewParent viewParent = view.getParent();
//    if (viewParent instanceof FrameLayout) {
//        final FrameLayout frameParent = (FrameLayout) viewParent;//整个父布局
//
//        final LinearLayout linearLayout = new LinearLayout(this);//新建一个LinearLayout
//        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        linearLayout.setOrientation(LinearLayout.VERTICAL);
//        linearLayout.setBackgroundResource(#88000000);//背景设置灰色透明
//        linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
//        linearLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                frameParent.removeView(linearLayout);
//            }
//        });
//
//        Rect rect = new Rect();
//        Point point = new Point();
//        nearby.getGlobalVisibleRect(rect, point);//获得nearby这个控件的宽高以及XY坐标 nearby这个控件对应就是需要高亮显示的地方
//
//        ImageView topGuideview = new ImageView(this);
//        topGuideview.setLayoutParams(new ViewGroup.LayoutParams(rect.width(), rect.height()));           topGuideview.setBackgroundResource(R.drawable.iv_topguide);
//
//        Rect rt = new Rect();
//        getWindow().getDecorView().getWindowVisibleDisplayFrame(rt);
//        topGuideview.setY(point.y - rt.top);//rt.top是手机状态栏的高度
//        ImageView bottomGuideview = new ImageView(this);
//        bottomGuideview.setLayoutParams(new ViewGroup.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
//        bottomGuideview.setBackgroundResource(R.drawable.iv_bottomguide);
//        bottomGuideview.setY(point.y + topGuideview.getHeight());
//
//        linearLayout.addView(topGuideview);
//        linearLayout.addView(bottomGuideview);
//        frameParent.addView(linearLayout);
//    }

}
