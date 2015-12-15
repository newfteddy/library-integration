package ru.umeta.libraryintegration.util

import java.security.MessageDigest

/**
 * Created by k.kosolapov on 12/8/2015.
 */
object ByteTo32SpreadAlgorithm {
    fun getHash(value: String): Int {
        val chars = value.toCharArray()
        var result = 0
        for (character in chars) {
            result = (result shl 8) + character.toByte()
        }
        //if the last bit is one shift it to the left on 16 bits.
        if ((result and 1) == 1) {
            result = result shl 16
        }
        return result
    }
}
