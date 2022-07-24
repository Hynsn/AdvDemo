package com.hynson.floatkkey;

import com.base.base.BaseActivity;
import com.hynson.R;
import com.hynson.databinding.ActivityCoroutineBinding;
import com.hynson.databinding.ActivityFloatkeyBinding;

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
