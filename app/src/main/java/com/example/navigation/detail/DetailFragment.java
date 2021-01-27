package com.example.navigation.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.R;
import com.example.base.BaseFragment;
import com.example.databinding.FragDetailBinding;

public class DetailFragment extends BaseFragment<FragDetailBinding,DetailVM> {

    public static DetailFragment newInstance() {
        return new DetailFragment();
    }

    @Override
    protected DetailVM getVm(ViewModelProvider provider) {
        return provider.get(DetailVM.class);
    }

    @Override
    protected FragDetailBinding getBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return FragDetailBinding.inflate(inflater,container,false);
    }

    @Override
    protected void bindView() {
        binding.btnBack.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_detailFragment_to_navigation_home));
    }

    @Override
    protected void initData() {

    }
}
