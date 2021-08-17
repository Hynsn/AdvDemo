package com.example.navigation.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.base.base.BaseFragment;
import com.example.R;
import com.example.databinding.FragHomeBinding;
import com.example.navigation.NavigationVM;

public class HomeFragment extends BaseFragment<FragHomeBinding,HomeViewModel> {
    final String TAG = HomeFragment.class.getSimpleName();

    private NavigationVM naviVM;

    /*@Override
    protected FragHomeBinding getBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return FragHomeBinding.inflate(inflater,container,false);
    }*/

    @Override
    protected int layoutId() {
        return R.layout.frag_home;
    }

    @Override
    protected HomeViewModel getVm(ViewModelProvider provider) {
        return provider.get(HomeViewModel.class);
    }

    @Override
    protected void bindView() {
        naviVM = new ViewModelProvider(getActivity()).get(NavigationVM.class);

        binding.btnJoin.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(v);

                String testName = binding.etName.getText().toString();
                // 传递数据方式1 bundle和argument
                Bundle bundle = new Bundle();
                bundle.putString("detail2",testName);
                // action可以传输argument，如是fragment id传输不了argument
                controller.navigate(R.id.action_navigation_home_to_detailFragment,bundle);
                //controller.navigate(R.id.detailFragment);
            }
        });
        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
                binding.textView.setText(s+"");
                binding.seekBar.setProgress(s);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        Log.i(TAG, "onDestroyView: ");
    }
}
