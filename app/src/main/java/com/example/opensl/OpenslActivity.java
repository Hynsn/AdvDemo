package com.example.opensl;

import android.content.res.AssetManager;
import android.view.View;

import com.base.base.BaseActivity;
import com.example.R;
import com.example.databinding.ActivityOpenslBinding;

public class OpenslActivity extends BaseActivity<ActivityOpenslBinding> {

    private AudioPlayer player;
    private AssetManager assetManager;

    @Override
    protected int getLayout() {
        return R.layout.activity_opensl;
    }

    @Override
    protected void bindView() {
        player = new AudioPlayer();
        assetManager = getAssets();
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
    }

    public void click(View v){
        switch (v.getId()){
            case R.id.btn_assetsfile:
                player.play_assets(assetManager, "audio/mydream.m4a");
                break;
            case R.id.btn_uri:
                player.play_uri("http://mpge.5nd.com/2015/2015-11-26/69708/1.mp3");
                break;
        }
    }
}