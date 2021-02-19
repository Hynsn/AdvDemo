#include <jni.h>
#include <string>
#include <iostream>

#include "mbedtls/include/version.h"
#include <android/log.h>
#include "myaes.h"

using namespace std;

#define UNIFORM_KEY        "a3K8Bx%2r8Y7#xDh"

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_mbedtls_AesUtil_version(
        JNIEnv *env, jobject /* this */) {
    return env->NewStringUTF(MBEDTLS_VERSION_STRING);
}
extern "C" JNIEXPORT jbyteArray JNICALL
Java_com_example_mbedtls_AesUtil_encrypt(
        JNIEnv *env, jobject /* this */, jbyteArray in, jint l) {
    unsigned char *dst;
    jbyte *js = env->GetByteArrayElements(in, 0);
    int ret = aes_ecb_encryption(&dst, UNIFORM_KEY, (const char *) (js), l);
    LogI("des: %s len: %d", dst, ret);;
    jbyteArray bytes = env->NewByteArray(ret);
    env->SetByteArrayRegion(bytes, 0, ret, (jbyte *) dst);
    if(dst) {
        free(dst);
        dst = nullptr;
    }
    if(js) env->ReleaseByteArrayElements(in,js,0);

    return bytes;
}

extern "C" JNIEXPORT jbyteArray JNICALL
Java_com_example_mbedtls_AesUtil_decrypt(
        JNIEnv *env, jobject /* this */, jbyteArray in, jint l) {
    unsigned char *dst;
    jbyte *js = env->GetByteArrayElements(in, 0);
    int ret = aes_ecb_decryption(&dst, UNIFORM_KEY, (const unsigned char *) js, l);
    jbyteArray bytes = nullptr;
    if(ret > 0){
        LogI("des: %s len: %d", dst, ret);
        bytes = env->NewByteArray(ret);
        env->SetByteArrayRegion(bytes, 0, ret, (jbyte *) dst);
        if(dst) {
            free(dst);
            dst = nullptr;
        }
    }
    if(js) env->ReleaseByteArrayElements(in,js,0);
    return bytes;
}