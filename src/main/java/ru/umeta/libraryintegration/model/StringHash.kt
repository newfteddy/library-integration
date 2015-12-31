package ru.umeta.libraryintegration.model

import gnu.trove.set.hash.TIntHashSet
import javax.persistence.Entity

/**
 * Created by ctash on 29.04.2015.
 */
@Entity
data class StringHash (var id: Long,
                       val tokens: TIntHashSet,
                       val simHash: Int) {

    fun hashPart1(): Byte {
        return ((simHash and 0xff000000.toInt()) ushr 24).toByte()
    }

    fun hashPart2(): Byte {
        return ((simHash and 0x00ff0000) ushr 16).toByte()
    }

    fun hashPart3(): Byte {
        return ((simHash and 0x0000ff00) ushr 8).toByte()
    }

    fun hashPart4(): Byte {
        return (simHash and 0x000000ff).toByte()
    }

    object Util {
        fun collectParts(vararg bytes: Byte): Int {
            if (bytes.size != 4) {
                throw IllegalArgumentException("The size of byte array is not 4.")
            }

            val hashPart1 = bytes[0]
            val hashPart2 = bytes[1]
            val hashPart3 = bytes[2]
            val hashPart4 = bytes[3]

            var result: Int = 0;
            result = (result shl 8) + hashPart1.toInt()
            result = (result shl 8) + hashPart2.toInt()
            result = (result shl 8) + hashPart3.toInt()
            result = (result shl 8) + hashPart4.toInt()

            return result
        }
    }
}
