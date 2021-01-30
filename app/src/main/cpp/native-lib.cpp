#include <jni.h>
#include <string>
#include <iostream>

#include "mbedtls/include/version.h"
#include <android/log.h>
#include "myaes.h"

using namespace std;

#define GREE_UNIFORM_KEY        "a3K8Bx%2r8Y7#xDh"

const unsigned char test1[32] = { 0x44, 0x41, 0x6A, 0xC2, 0xD1, 0xF5, 0x3C, 0x58,
                  0x33, 0x03, 0x91, 0x7E, 0x6B, 0xE9, 0xEB, 0xE0,0xF3, 0xF6, 0x75, 0x2A, 0xE8, 0xD7, 0x83, 0x11,
        0x38, 0xF0, 0x41, 0x56, 0x06, 0x31, 0xB1, 0x14};
const unsigned char defa[45] = "tsuakVelcpo6GoxNcBJEf10uMqJuW4bHiyGAiolfPLY=";
extern "C" JNIEXPORT jstring JNICALL
Java_com_example_mbedtls_NdkUtil_version(
        JNIEnv* env, jobject /* this */) {
    __android_log_print(ANDROID_LOG_VERBOSE, LOG_TGA, "测试事件");
    __android_log_print(ANDROID_LOG_VERBOSE, LOG_TGA, "number: %d",mbedtls_version_get_number());
    string dd = "测试按钮";
    char test[16] = "abc";
    mbedtls_version_get_string(test);
    mbedtls_base64_self_test(1);
    mbedtls_aes_ecb_self_test(1);

    aes_ecb_encryption(GREE_UNIFORM_KEY, reinterpret_cast<const char *>(test1));
//    aes_ecb_decryption(GREE_UNIFORM_KEY,defa,strlen((const char *)(defa)));
    //sprintf(test,"number %x",mbedtls_version_get_number());
    return env->NewStringUTF(test);
}