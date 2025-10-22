package com.hynson.nfc

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

fun String.fitByteArray(size: Int): ByteArray {
    val decodedBytes = this.toByteArray()
    val extendedBytes = ByteArray(size)
    System.arraycopy(decodedBytes, 0, extendedBytes, 0, decodedBytes.size)
    return extendedBytes
}

object AESUtil {
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
        return Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
    }

    fun decrypt(encryptedData: String, key: ByteArray): String {
        if (key.size != 16 && key.size != 24 && key.size != 32) {
            throw IllegalArgumentException("Key length must be 16, 24, or 32 bytes")
        }
        val secretKey = SecretKeySpec(key, AES)
        val cipher = Cipher.getInstance(AES_ECB_PKCS7_PADDING)
        cipher.init(Cipher.DECRYPT_MODE, secretKey)
        val decodedBytes = Base64.decode(encryptedData, Base64.DEFAULT)
        val decryptedBytes = cipher.doFinal(decodedBytes)
        return String(decryptedBytes)
    }
}
