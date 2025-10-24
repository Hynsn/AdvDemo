package com.hynson.nfc

import android.util.Base64
import android.util.Log
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

fun String.fitByteArray(size: Int): ByteArray {
    val decodedBytes = this.toByteArray()
    val extendedBytes = ByteArray(size)
    System.arraycopy(decodedBytes, 0, extendedBytes, 0, decodedBytes.size)
    return extendedBytes
}

object AESUtil {
    private const val TAG = "AESUtil"

    private const val AES = "AES"
    private const val AES_ECB_PKCS7_PADDING = "AES/ECB/PKCS7Padding"

    fun encrypt(data: String, key: ByteArray): String {
        if (key.size != 16 && key.size != 24 && key.size != 32) {
            throw IllegalArgumentException("Key length must be 16, 24, or 32 bytes")
        }
        val secretKey = SecretKeySpec(key, AES)
        val cipher = Cipher.getInstance(AES_ECB_PKCS7_PADDING)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val encryptedBytes = cipher.doFinal(data.toByteArray())
        return Base64.encodeToString(encryptedBytes, Base64.NO_WRAP)
    }

    fun decrypt(encryptedData: String, key: ByteArray): String {
        if (key.size != 16 && key.size != 24 && key.size != 32) {
            throw IllegalArgumentException("Key length must be 16, 24, or 32 bytes")
        }
        val secretKey = SecretKeySpec(key, AES)
        val cipher = Cipher.getInstance(AES_ECB_PKCS7_PADDING)
        cipher.init(Cipher.DECRYPT_MODE, secretKey)
        val decodedBytes = Base64.decode(encryptedData, Base64.NO_WRAP)
        val decryptedBytes = cipher.doFinal(decodedBytes)
        return String(decryptedBytes)
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun createPwd(uid: ByteArray): Pair<ByteArray, ByteArray> {
        val tag = "VeoRideNTAG"
        val old = uid + tag.toByteArray()
        Log.i(TAG, "数据: ${old.toHexString()}")
        val md = MessageDigest.getInstance("MD5")
        // 对输入字符串进行哈希处理
        val digest = md.digest(old)
        Log.i(TAG, "md5: ${digest.toHexString()}")
        val size = digest.size
        val pwd = digest.copyOfRange(0, 4)
        val pack = digest.copyOfRange(size - 2, size)
        Log.i(TAG, "pwd: ${pwd.toHexString()}, pack: ${pack.toHexString()}")
        return Pair(pwd, pack)
    }
}
