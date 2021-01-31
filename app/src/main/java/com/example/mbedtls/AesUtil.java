package com.example.mbedtls;

public class AesUtil {
    static {
        System.loadLibrary("native-lib");
    }
    public native String version();
    public native byte[] encrypt(byte[] s,int len);
    public native byte[] decrypt(byte[] s,int len);
}
