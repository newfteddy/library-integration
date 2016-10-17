package ru.umeta.libraryintegration.util

import java.nio.ByteBuffer
import java.security.MessageDigest

object MD5To32Algorithm {
    fun getHash(value: String): Int {
        val messageDigest = MessageDigest.getInstance("MD5")
        val bytes = messageDigest.digest(value.toByteArray())
        var result = 0
        for (byte in bytes) {
            result = (result shl 8) + byte
        }
        return result;
    }
}
