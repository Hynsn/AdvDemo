package com.hynson.navigation;

import android.util.Log;

import com.fastdroid.base.BaseActivity;
import com.hynson.R;
import com.hynson.databinding.ActivityNaviLoadBinding;

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
