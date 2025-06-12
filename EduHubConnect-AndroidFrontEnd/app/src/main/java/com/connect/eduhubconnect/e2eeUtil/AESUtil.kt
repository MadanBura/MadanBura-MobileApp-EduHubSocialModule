package com.connect.eduhubconnect.e2eeUtil

import android.os.Build
import android.util.Base64
import androidx.annotation.RequiresApi
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

object AESUtil {
    fun generateAESKey(): SecretKey {
        val keyGen = KeyGenerator.getInstance("AES")
        keyGen.init(256)
        return keyGen.generateKey()
    }

    fun encrypt(text: String, secretKey: SecretKey): String {
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val encrypted = cipher.doFinal(text.toByteArray(Charsets.UTF_8))
        return Base64.encodeToString(encrypted, Base64.DEFAULT)
    }

    fun decrypt(encrypted: String, secretKey: SecretKey): String {
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.DECRYPT_MODE, secretKey)
        val decodedBytes = Base64.decode(encrypted, Base64.DEFAULT)
        return String(cipher.doFinal(decodedBytes), Charsets.UTF_8)
    }

    fun secretKeyFromBytes(keyBytes: ByteArray): SecretKey {
        return SecretKeySpec(keyBytes, 0, keyBytes.size, "AES")
    }
}
