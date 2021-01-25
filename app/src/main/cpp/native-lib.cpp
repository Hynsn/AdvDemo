#include <jni.h>
#include <string>
#include <iostream>

#include "mbedtls/include/version.h"

#include <android/log.h>

const char * LOG_TGA = "LOG_TGA";

using namespace std;

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_mbedtls_NdkUtil_version(
        JNIEnv* env, jobject /* this */) {
    __android_log_print(ANDROID_LOG_VERBOSE, LOG_TGA, "测试事件");
    __android_log_print(ANDROID_LOG_VERBOSE, LOG_TGA, "number: %d",mbedtls_version_get_number());
    string dd = "测试按钮";
    char test[16] = "abc";
    mbedtls_version_get_string(test);
    //sprintf(test,"number %x",mbedtls_version_get_number());
    return env->NewStringUTF(test);
}