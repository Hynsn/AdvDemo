package com.example.mbedtls;

public class NdkUtil {
    static {
        System.loadLibrary("native-lib");
    }
    public native String version();
}
