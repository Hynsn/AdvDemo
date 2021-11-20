package com.example.navigation;

import android.util.Log;

import com.base.base.BaseActivity;
import com.example.R;
import com.example.databinding.ActivityNaviLoadBinding;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

/**
 * Author: Hynsonhou
 * Date: 2021/8/10 14:46
 * Description: 路由入口
 * History:
 * <author>  <time>     <version> <desc>
 * Hynsonhou  2021/8/10   1.0       首次创建
 */
public class NaviLoadActivity extends BaseActivity<ActivityNaviLoadBinding> {
    @Override
    protected int getLayout() {
        return R.layout.activity_navi_load;
    }

    private ActivityVM naviVM;
    @Override
    protected void bindView() {
        naviVM = new ViewModelProvider(this).get(ActivityVM.class);
        Log.i(TAG, "bindView: "+System.identityHashCode(naviVM));
        naviVM.getName().observeForever(s -> {
            Log.i(TAG, "bindView: "+s);
            binding.btnLoad.setText(s);
        });
        binding.btnLoad.setOnClickListener(v -> {
            
        });
    }
    final String TAG = NaviLoadActivity.class.getSimpleName();
}
