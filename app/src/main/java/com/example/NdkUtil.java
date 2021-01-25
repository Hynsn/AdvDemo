package com.example;

public class NdkUtil {
    static {
        System.loadLibrary("native-lib");
    }
    public native String stringFromJNI();
}
