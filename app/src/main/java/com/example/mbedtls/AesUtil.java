package com.example.mbedtls;

public class AesUtil {
    static {
        System.loadLibrary("native-lib");
    }
    private static AesUtil instance;

    public AesUtil() {}

    public static synchronized AesUtil getInstance(){
        if(instance==null){
            instance = new AesUtil();
        }
        return instance;
    }

    public native String version();
    public native byte[] encrypt(byte[] in,int len);
    public native byte[] decrypt(byte[] in,int len);
}
