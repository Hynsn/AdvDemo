package com.example.customview;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
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

        updateAlarmMinuteView(mBuilder.toString());
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
                binding.ntText.del();

                if(mBuilder.length() > 0){
                    mBuilder.deleteCharAt(mBuilder.length() - 1);
                    updateAlarmMinuteView(mBuilder.toString());
                }
                break;
            case R.id.btn_append:
                int max = 57,min = 48;
                int random = (int) (Math.random()*(max-min)+min);

                binding.ntText.appCode(random);

                mBuilder.appendCodePoint(random);
                if(mBuilder.length() > 3){
                    mBuilder.deleteCharAt(0);
                }
                updateAlarmMinuteView(mBuilder.toString());
                break;
        }
    }
    private StringBuilder mBuilder = new StringBuilder();

    private void updateAlarmMinuteView(String text){
        String format = String.format("%0" + 3 + "d", Integer.parseInt(TextUtils.isEmpty(text) ? "0": text));
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(format+"\b");
        int lastIndex = format.lastIndexOf('0');
        Log.i(TAG, "updateAlarmMinuteView: "+format+","+lastIndex);
        for(int i=0; i < format.length() ; i++){
            NumSpan hotSpan = new NumSpan(getApplicationContext(), lastIndex < i ? R.color.numColor:R.color.zeroColor);
            Log.i(TAG, "updateAlarmMinuteView: "+i+","+(i+1));
            builder.setSpan(hotSpan, i, i+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        binding.tvNumb.setText(builder);
    }
}
