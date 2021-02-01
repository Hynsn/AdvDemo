//
// Created by housaibang on 2021-01-28.
//

#ifndef TESTDEMO_MYAES_H
#define TESTDEMO_MYAES_H

#include <stddef.h>
#include <string.h>
#include <math.h>

#include "mbedtls/include/base64.h"
#include "mbedtls/include/aes.h"
#include "mbedtls/include/platform.h"

#include "log.h"

int hex2_string(unsigned char *src, int srclen, char *des, int deslen) {
    unsigned char Hb;
    unsigned char Lb;

    if (deslen < srclen * 2) return 0;
    memset(des, 0, deslen);

    for (int i = 0; i < srclen; i++) {
        Hb = (src[i] & 0xF0) >> 4; // 高位
        if (Hb <= 9) Hb += 0x30; // 0~9
        else if (Hb >= 10 && Hb <= 15) Hb = Hb - 10 + 'a'; // 10~15
        else return 0;

        Lb = src[i] & 0x0F; // 低位
        if (Lb <= 9) Lb += 0x30;
        else if (Lb >= 10 && Lb <= 15) Lb = Lb - 10 + 'a';
        else return 0;

        des[i * 2] = Hb;
        des[i * 2 + 1] = Lb;
    }
    return 1;
}

#define ECB_BLOCK_SIZE 16

int aes_ecb_encryption(unsigned char **dst, const char *key, const char *src, size_t slen) {
    int ret = -1;

    size_t ecb_time, remain, dlen,block_len;
    u_char cover;

    u_char *tmp_buf, *base64_buf;

    char ecb_key[17] = {0};
    memset(ecb_key, 0, sizeof(ecb_key));
    strcpy(ecb_key, key);

    ecb_time = slen / ECB_BLOCK_SIZE;
    remain = slen % ECB_BLOCK_SIZE;
    cover = ECB_BLOCK_SIZE - remain;

    mbedtls_aes_context ctx;
    mbedtls_aes_init(&ctx);
    ret = mbedtls_aes_setkey_enc(&ctx, reinterpret_cast<const unsigned char *>(ecb_key), 128);

    if (ret != 0) {
        LogI("aes_ecb init failed.");
        return ret;
    }

    if (remain != 0) ecb_time++;
    block_len = ecb_time * ECB_BLOCK_SIZE;
    tmp_buf = (u_char *) malloc(sizeof(u_char) * (block_len));

    if(tmp_buf != nullptr){
        memset(tmp_buf, 0, block_len);
        for (size_t i = 0; i < block_len; i++) {
            if (i >= slen) tmp_buf[i] = cover;
            else tmp_buf[i] = src[i];
        }
        for (int j = 0; j < ecb_time; j++) {
            ret = mbedtls_aes_crypt_ecb(&ctx, 1, &tmp_buf[16 * j], &tmp_buf[16 * j]);
        }
        tmp_buf[block_len] = '\0';
        if(ret != 0){
            LogI("aes_ecb ecb error.");
        }
    }
    else{
        LogI("malloc failed.");
    }

    int base_len = ceil((float) block_len / 3) * 4 + 1;
    base64_buf = (u_char *) malloc(sizeof(u_char) * (base_len));
    if(base64_buf != nullptr){
        memset(base64_buf, 0, base_len);
        ret = mbedtls_base64_encode(base64_buf, base_len, &dlen, tmp_buf, block_len);
        if(ret!=0){
            LogI("base64 encode error.");
        }
        *dst = base64_buf;
    }
    else{
        LogI("malloc failed.");
    }

    if (tmp_buf != nullptr) {
        LogI("tmp_buf free.");
        free(tmp_buf);
        tmp_buf = nullptr;
    }
    mbedtls_aes_free(&ctx);
    LogI("buf: %skey: %s etlen: %d ptlen: %d slen: %d",
         base64_buf, ecb_key, dlen, block_len, slen);

    return ret == 0 ? dlen : -1;
}

int aes_ecb_decryption(unsigned char **dst, const char *key, const unsigned char *src, size_t slen) {
    size_t ecb_time = 0,de_len = 0;
    int ret = -1;

    unsigned char *tmp_buf;

    char ecb_key[17] = {0};
    memset(ecb_key, 0, sizeof(ecb_key));
    strcpy(ecb_key, key);

    size_t buf_len = ((slen / 4) * 3) + 1;
    LogI("buf_len: %ld slen: %ld", buf_len, slen);;
    ecb_time = slen / ECB_BLOCK_SIZE;

    if ((slen < ECB_BLOCK_SIZE) && (slen != 0)) {
        LogI("slen <16 and >0");
        return ret;
    }

    mbedtls_aes_context ctx;
    ret = mbedtls_aes_setkey_dec(&ctx, reinterpret_cast<const unsigned char *>(ecb_key), 128);
    if (ret != 0) {
        LogI("aes_ecb init failed.");
        return ret;
    }

    tmp_buf = (unsigned char *) malloc(sizeof(char) * buf_len);
    if(tmp_buf != nullptr){
        memset(tmp_buf, 0, buf_len);
        ret = mbedtls_base64_decode(tmp_buf, buf_len, &de_len, src, slen);
        if(ret==0){
            for (int j = 0; j < ecb_time && (j * 16 < de_len); j++) {
                ret = mbedtls_aes_crypt_ecb(&ctx, 0, &tmp_buf[16 * j], &tmp_buf[16 * j]);
            }
            if(ret != 0){
                LogI("aes_ecb ecb error.");
            }
            *dst = tmp_buf;
        }
    }
    else {
        LogI("malloc failed.");
        ret = -1;
    }

    mbedtls_aes_free(&ctx);

    return ret == 0 ? de_len : -1;
}

#endif //TESTDEMO_MYAES_H
