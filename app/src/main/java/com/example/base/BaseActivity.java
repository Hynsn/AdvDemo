package com.example.base;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import java.lang.reflect.Method;

public abstract class BaseActivity<V extends ViewDataBinding> extends AppCompatActivity {
    final static String TAG = BaseActivity.class.getSimpleName();
    protected V binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, getLayout());
        try {
            Method setMethod = binding.getClass().getMethod("setActivity",getClass());
            setMethod.invoke(binding,this);
            Log.i(TAG, "invoke success");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(binding!=null) bindView();
    }

    protected abstract int getLayout();
    protected abstract void bindView();
}
