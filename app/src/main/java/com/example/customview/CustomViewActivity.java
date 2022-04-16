package com.example.customview;

import com.base.base.BaseActivity;
import com.example.R;
import com.example.databinding.ActivityCustomviewBinding;

public class CustomViewActivity extends BaseActivity<ActivityCustomviewBinding> {
    final static String TAG = CustomViewActivity.class.getSimpleName();

    @Override
    protected int getLayout() {
        return R.layout.activity_customview;
    }
    @Override
    protected void bindView() {

    }
}
