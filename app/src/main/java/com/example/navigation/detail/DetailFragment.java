package com.example.navigation.detail;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.base.base.BaseFragment;
import com.example.R;
import com.example.databinding.FragDetailBinding;
import com.example.navigation.NavigationVM;

public class DetailFragment extends BaseFragment<FragDetailBinding, DetailVM> {

    final String TAG = DetailFragment.class.getSimpleName();

    public static DetailFragment newInstance() {
        return new DetailFragment();
    }

    @Override
    protected DetailVM getVm(ViewModelProvider provider) {
        return provider.get(DetailVM.class);
    }

/*    @Override
    protected FragDetailBinding getBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return FragDetailBinding.inflate(inflater, container, false);
    }*/

    @Override
    protected int layoutId() {
        return R.layout.frag_detail;
    }

    private NavigationVM naviVM;

    @Override
    protected void bindView() {
        naviVM = new ViewModelProvider(getActivity()).get(NavigationVM.class);

        // 该方法正式应用，需要配置pop behavior或者使用下属方法
        //binding.btnBack.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_detailFragment_to_navigation_home));
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController controller = Navigation.findNavController(view);
                controller.navigateUp();
            }
        });
        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(v);
                controller.navigate(R.id.setFrag);
            }
        });

        String detail1 = getArguments().getString("detail1");
        String detail2 = getArguments().getString("detail2");
        binding.tvDetail1.setText(detail1);
        binding.tvDetail2.setText(detail2);

        binding.sbProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                naviVM.getProgress().setValue(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    protected void initData(LifecycleOwner owner, Bundle savedInstanceState) {
        naviVM.getProgress().observe(owner, new Observer<Integer>() {
            @Override
            public void onChanged(Integer s) {
                binding.tvDetail3.setText(s+"");
                binding.sbProgress.setProgress(s);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: ");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        Log.i(TAG, "onDestroyView: ");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.i(TAG, "onDestroy: ");
    }
}
