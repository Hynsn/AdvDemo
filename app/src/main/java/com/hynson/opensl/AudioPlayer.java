package com.hynson.opensl;

import android.content.res.AssetManager;

public class AudioPlayer {
    /**
     *
     * java层提供pcm数据，opensl底层播放
     *
     * 只是演示播放方式，停止回收资源逻辑自行设计
     *
     * @param data
     * @param size
     */
    static {
        System.loadLibrary("native-lib");
    }
    public native void sendPcmData(byte[] data, int size);
    public native void play_assets(AssetManager assetManager, String filename);

    public native void play_uri(String uri);

    public native void play_pcm(String pamPath);
}
