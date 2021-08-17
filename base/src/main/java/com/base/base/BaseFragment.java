package com.base.base;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import java.lang.reflect.Method;

public abstract class BaseFragment<V extends ViewDataBinding, VM extends FviewModel> extends Fragment {
    final static String TAG = BaseActivity.class.getSimpleName();
    protected V binding;
    protected VM vm;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelProvider provider = new ViewModelProvider(this);
        vm = getVm(provider);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (binding == null) {

            binding = DataBindingUtil.inflate(inflater, layoutId(), container, false);

            //binding = getBinding(inflater, container, savedInstanceState);
            try {
                Method setMethod = binding.getClass().getMethod("setActivity", getClass());
                setMethod.invoke(binding, this);
                Log.i(TAG, "invoke success");
            } catch (Exception e) {
                e.printStackTrace();
            }
            bindView();

            vm.registLifeOwner(getViewLifecycleOwner());
        }
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedState) {
        super.onViewCreated(view, savedState);
        initData(getViewLifecycleOwner(),savedState);
    }

    @Override
    public void onDestroy() {
        if (binding != null) {
            ViewGroup parent = (ViewGroup) binding.getRoot().getParent();
            if (parent != null) parent.removeView(binding.getRoot());
            binding = null;
        }
        super.onDestroy();
    }

    protected abstract int layoutId();

    //protected abstract V getBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    protected abstract VM getVm(ViewModelProvider provider);

    protected abstract void bindView();

    protected abstract void initData(LifecycleOwner owner, Bundle savedInstanceState);
}
