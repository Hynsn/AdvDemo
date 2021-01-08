package com.example.customview;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.R;
import com.example.base.BaseActivity;
import com.example.databinding.ActivityTimelineBinding;

import java.util.ArrayList;
import java.util.List;

public class TimeLineActivity extends BaseActivity<ActivityTimelineBinding> {
    final static String TAG = TimeLineActivity.class.getSimpleName();
    private TimeProgressBar timePB;
    private Button nextBtn,resetBtn;
    private int progress = 0;
    List<Integer> stepSeconds;

    @Override
    protected int getLayout() {
        return R.layout.activity_timeline;
    }

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
    }

    public void click(View v){
        Log.i(TAG, "click: ");
    }
}
