package com.example.floatkkey;

import android.view.View;
import android.widget.LinearLayout;

import com.base.base.BaseActivity;
import com.example.R;
import com.example.databinding.ActivityCoroutineBinding;
import com.example.databinding.ActivityFloatkeyBinding;
import com.example.gson.TestBean;
import com.example.gson.TestTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class FloatKeyActivity extends BaseActivity<ActivityFloatkeyBinding> {
    @Override
    protected int getLayout() {
        return R.layout.activity_floatkey;
    }

    @Override
    protected void bindView() {
        initFloatBtn();
    }

    private void initFloatBtn() {
        FloatBtnUtil  floatBtnUtil = new FloatBtnUtil(this);
        floatBtnUtil.setFloatView(binding.root,binding.fab);
    }

}
