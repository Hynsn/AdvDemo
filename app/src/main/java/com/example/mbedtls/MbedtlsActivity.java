package com.example.mbedtls;

import android.view.View;

import com.example.R;
import com.example.base.BaseActivity;
import com.example.databinding.ActivityMbedtlsBinding;

public class MbedtlsActivity extends BaseActivity<ActivityMbedtlsBinding> {

    @Override
    protected int getLayout() {
        return R.layout.activity_mbedtls;
    }

    @Override
    protected void bindView() {
        binding.tvVersion.setText(new NdkUtil().version());
    }
    public void click(View v){

    }
}
