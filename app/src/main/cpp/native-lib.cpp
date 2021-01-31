#include <jni.h>
#include <string>
#include <iostream>

#include "mbedtls/include/version.h"
#include <android/log.h>
#include "myaes.h"

using namespace std;

#define GREE_UNIFORM_KEY        "a3K8Bx%2r8Y7#xDh"

const unsigned char test1[16] = { 0x44, 0x41, 0x6A, 0xC2, 0xD1, 0xF5, 0x3C, 0x58,
                  0x33, 0x03, 0x91, 0x7E, 0x6B, 0xE9,0x6B,0x6B};
const unsigned char defa[45] = "2kGvBf/RoKqFzQpx/6/qXA==";
extern "C" JNIEXPORT jstring JNICALL
Java_com_example_mbedtls_AesUtil_version(
        JNIEnv* env, jobject /* this */) {
    char test[16] = "abc";
    mbedtls_version_get_string(test);
    __android_log_print(ANDROID_LOG_VERBOSE, LOG_TGA, "number: %d",mbedtls_version_get_number());
    return env->NewStringUTF((const char *)(test));
}
extern "C" JNIEXPORT jbyteArray JNICALL
Java_com_example_mbedtls_AesUtil_encrypt(
        JNIEnv* env,jobject /* this */,jbyteArray s,jint l) {
    /*mbedtls_base64_self_test(1);
    mbedtls_aes_ecb_self_test(1);*/

    unsigned char* des;
    /*unsigned char dest[33];
    memset(des,0, 33);
    hex2_string(dest, 16, reinterpret_cast<char *>(s), l);
    __android_log_print(ANDROID_LOG_VERBOSE, LOG_TGA, "buf: %s ",dest);*/
//    unsigned char* ts;
    jbyte *js = env->GetByteArrayElements(s, 0);
    int ret = aes_ecb_encryption(&des, GREE_UNIFORM_KEY, (const char *)(js), l);
    /*
    aes_ecb_decryption(&des,GREE_UNIFORM_KEY,defa,strlen((const char *)(defa)));
     */
    __android_log_print(ANDROID_LOG_VERBOSE, LOG_TGA, "des: %s len: %d", des,ret);

    /*if(des!= nullptr){
        free(des);
        des = nullptr;
    }*/
    jbyteArray bytes = env->NewByteArray(ret);
    env->SetByteArrayRegion(bytes,0,ret,(jbyte*)des);
    return bytes;
}

extern "C" JNIEXPORT jbyteArray JNICALL
Java_com_example_mbedtls_AesUtil_decrypt(
        JNIEnv* env,jobject /* this */,jbyteArray s,jint l) {
    unsigned char* des;
    jbyte *js = env->GetByteArrayElements(s, 0);
    int ret = aes_ecb_decryption(&des,GREE_UNIFORM_KEY,(const unsigned char*)js,l);
    __android_log_print(ANDROID_LOG_VERBOSE, LOG_TGA, "des: %s len: %d", des,ret);
    /*if(des!= nullptr){
        free(des);
        des = nullptr;
    }*/
    jbyteArray bytes = env->NewByteArray(ret);
    env->SetByteArrayRegion(bytes,0,ret,(jbyte*)des);
    return bytes;
}