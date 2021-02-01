//
// Created by housaibang on 2021-02-01.
//

#ifndef _LOG_H
#define _LOG_H

#define TAG "ndk"

#define LogV(...) __android_log_print(ANDROID_LOG_VERBOSE, TAG, __VA_ARGS__)
#define LogD(...) __android_log_print(ANDROID_LOG_DEBUG, TAG, __VA_ARGS__)
#define LogI(...) __android_log_print(ANDROID_LOG_INFO, TAG, __VA_ARGS__)
#define LogW(...) __android_log_print(ANDROID_LOG_WARN, TAG, __VA_ARGS__)
#define LogE(...) __android_log_print(ANDROID_LOG_ERROR, TAG, __VA_ARGS__)

#endif //_LOG_H
