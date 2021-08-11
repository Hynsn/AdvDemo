package com.example.navigation;

import android.view.View;

import com.example.R;
import com.example.base.BaseActivity;
import com.example.databinding.ActivityNaviLoadBinding;


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

    @Override
    protected void bindView() {
        binding.btnLoad.setOnClickListener(v -> {
            
        });
    }
}
