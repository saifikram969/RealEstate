package com.btjnonbrokerage.Base

import android.util.Base64
import java.security.MessageDigest
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object HashGenerationUtils {

    fun generateHashFromSDK(
        hashData: String,
        salt: String?,
        merchantSecretKey: String? = null
    ): String? {
        return if (merchantSecretKey.isNullOrEmpty()) {
            calculateHash("$hashData$salt") // SHA-512 hash with data and salt
        } else {
            calculateHmacSha1(hashData, merchantSecretKey) // HMAC SHA1 with key
        }
    }

    fun generateV2HashFromSDK(
        hashString: String,
        salt: String?
    ): String? {
        return calculateHmacSha256(hashString, salt) // HMAC SHA256
    }

    private fun calculateHash(hashString: String): String {
        val messageDigest = MessageDigest.getInstance("SHA-512")
        messageDigest.update(hashString.toByteArray(Charsets.UTF_8))
        val mdBytes = messageDigest.digest()
        return mdBytes.joinToString("") { "%02x".format(it) }
    }

    private fun calculateHmacSha1(hashString: String, key: String): String? {
        return try {
            val type = "HmacSHA1"
            val secret = SecretKeySpec(key.toByteArray(Charsets.UTF_8), type)
            val mac = Mac.getInstance(type)
            mac.init(secret)
            val bytes = mac.doFinal(hashString.toByteArray(Charsets.UTF_8))
            bytes.joinToString("") { "%02x".format(it) }
        } catch (e: Exception) {
            null
        }
    }



    private fun calculateHmacSha256(hashString: String, salt: String?): String? {
        return try {
            val type = "HmacSHA256"
            val secret = SecretKeySpec(salt?.toByteArray(Charsets.UTF_8), type)
            val mac = Mac.getInstance(type)
            mac.init(secret)
            val bytes = mac.doFinal(hashString.toByteArray(Charsets.UTF_8))
            Base64.encodeToString(bytes, Base64.NO_WRAP)
        } catch (e: Exception) {
            null
        }
    }

}
