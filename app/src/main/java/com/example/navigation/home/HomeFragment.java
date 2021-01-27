package com.example.navigation.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.R;
import com.example.base.BaseFragment;
import com.example.databinding.FragHomeBinding;

public class HomeFragment extends BaseFragment<FragHomeBinding,HomeViewModel> {

    @Override
    protected FragHomeBinding getBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return FragHomeBinding.inflate(inflater,container,false);
    }

    @Override
    protected HomeViewModel getVm(ViewModelProvider provider) {
        return provider.get(HomeViewModel.class);
    }

    @Override
    protected void bindView() {
        vm.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                binding.textHome.setText(s);
            }
        });

        binding.textHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(v);
                controller.navigate(R.id.action_navigation_home_to_detailFragment);
            }
        });
    }

    @Override
    protected void initData() {

    }
}
