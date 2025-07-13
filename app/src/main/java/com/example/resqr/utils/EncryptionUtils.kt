package com.example.resqr.utils

import android.util.Base64
import com.example.resqr.BuildConfig
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

object EncryptionUtils {
    private const val SECRET_KEY = BuildConfig.SECRET_KEY

    fun encrypt(input: String): String {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val keySpec = generateKey() // ← now simpler
        cipher.init(Cipher.ENCRYPT_MODE, keySpec)
        val encryptedBytes = cipher.doFinal(input.toByteArray())
        return Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
    }

    fun decrypt(input: String): String {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val keySpec = generateKey()
        cipher.init(Cipher.DECRYPT_MODE, keySpec)
        val decodedBytes = Base64.decode(input, Base64.DEFAULT)
        return String(cipher.doFinal(decodedBytes))
    }

    private fun generateKey(): SecretKeySpec {
        val digest = MessageDigest.getInstance("SHA-256")
        val bytes = digest.digest(SECRET_KEY.toByteArray(Charsets.UTF_8))
        return SecretKeySpec(bytes, "AES")
    }
}
