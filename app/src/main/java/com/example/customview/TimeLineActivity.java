package com.example.customview;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import com.example.R;
import com.example.base.BaseActivity;
import com.example.databinding.ActivityTimelineBinding;

import java.util.ArrayList;
import java.util.List;

public class TimeLineActivity extends BaseActivity<ActivityTimelineBinding> {
    final static String TAG = TimeLineActivity.class.getSimpleName();
    private int progress = 0;
    List<Integer> stepSeconds;

    @Override
    protected int getLayout() {
        return R.layout.activity_timeline;
    }

    @SuppressLint("RtlHardcoded")
    @Override
    protected void bindView() {
        stepSeconds = new ArrayList<>();
        stepSeconds.add(2);
        stepSeconds.add(2);
        stepSeconds.add(2);
        stepSeconds.add(2);
        stepSeconds.add(2);

        stepSeconds.add(2);
        stepSeconds.add(2);
        stepSeconds.add(4);
        stepSeconds.add(4);
        stepSeconds.add(4);
        stepSeconds.add(4);
        stepSeconds.add(4);

        binding.timePb.setStep(9,stepSeconds);
        binding.tvNumber.setGravity(Gravity.RIGHT);
    }

    public void click(View v){
        Log.i(TAG, "click: ");
        switch (v.getId()){
            case R.id.btn_reset:
                progress = 0;
                binding.timePb.setCurrentPos(progress);
                break;
            case R.id.btn_next:
                if(progress>20) {
                    binding.timePb.setCurrentPos(progress);
                    progress++;
                }
                break;
            case R.id.btn_del:
                Editable editable = binding.tvNumber.getText();
                binding.tvNumber.setSelection(editable.length());
                int start = binding.tvNumber.getSelectionStart();
                if (start > 0) {
                    editable.delete(start - 1, start);
                }
                break;
        }
    }
}
