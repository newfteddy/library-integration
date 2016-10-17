package ru.umeta.libraryintegration.model

/**
 * Created by ctash on 29.04.2015.
 */
data class StringHash(val simHash: Int) {

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

}
