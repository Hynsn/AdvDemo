package com.hynson.topbar;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.fastdroid.base.BaseActivity;
import com.hynson.R;
import com.hynson.databinding.ActivityTopbarBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Hynsonhou
 * Date: 2021/11/29 21:13
 * Description: 吸顶栏
 * History:
 * <author>  <time>     <version> <desc>
 * Hynsonhou  2021/11/29   1.0       首次创建
 */
public class TopBarActivity extends BaseActivity<ActivityTopbarBinding> {

    private List<String> list;
    private NormalAdapter normalAdapter;
    private LinearLayoutManager linearLayoutManager,linearLayoutManager1;

    @Override
    protected int getLayout() {
        return R.layout.activity_topbar;
    }

    @Override
    protected void bindView() {
        initList();
        initView();

        binding.scrollTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = linearLayoutManager.findViewByPosition(5);
                binding.nsvScroll.scrollTo(0,view.getTop());
            }
        });
    }

    private void initList() {
        list = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            list.add(i + "");
        }
    }

    private void initView() {
        linearLayoutManager = new LinearLayoutManager(this);
        normalAdapter = new NormalAdapter(list);

        binding.recyclerView.setLayoutManager(linearLayoutManager);
        binding.recyclerView.setNestedScrollingEnabled(false);
        binding.recyclerView.setAdapter(normalAdapter);

        linearLayoutManager1 = new LinearLayoutManager(this);
        normalAdapter = new NormalAdapter(list);
        binding.recyclerView1.setLayoutManager(linearLayoutManager1);
        binding.recyclerView1.setNestedScrollingEnabled(false);
        binding.recyclerView1.setAdapter(normalAdapter);
    }
}
